package com.wevx.dealershipmanagement.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.service.LocationForegroundService
import com.wevx.dealershipmanagement.databinding.FragmentHomeBinding
import com.wevx.dealershipmanagement.domain.models.AreaModel
import com.wevx.dealershipmanagement.domain.models.DistrictModel
import com.wevx.dealershipmanagement.domain.models.Divisions
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel
import com.wevx.dealershipmanagement.domain.models.SubDistrictModel
import com.wevx.dealershipmanagement.presentation.adapter.CustomSpinnerAdapter
import com.wevx.dealershipmanagement.presentation.adapter.StoreOwnerAdapter
import com.wevx.dealershipmanagement.presentation.home.getArea.AreaViewModel
import com.wevx.dealershipmanagement.presentation.home.getDistrict.DistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getDivision.DivisionViewModel
import com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByArea.StoreOwnerViewModel
import com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByDistrict.StoreOwnerByDistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getSubDistrict.SubDistrictViewModel
import com.wevx.dealershipmanagement.utils.Constants
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisions
import com.wevx.dealershipmanagement.utils.handleMultiplePermissionsResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    StoreOwnerAdapter.HandleCustomerClickListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val divisionViewModel: DivisionViewModel by viewModels()
    private val districtViewModel: DistrictViewModel by viewModels()
    private val subDistrictViewModel: SubDistrictViewModel by viewModels()
    private val areaViewModel: AreaViewModel by viewModels()
    private val storeOwnerViewModel: StoreOwnerViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val storeByDisViewModel: StoreOwnerByDistrictViewModel by viewModels()

    private lateinit var storeOwnerAdapter: StoreOwnerAdapter

    private var currentDivisions: List<Divisions> = emptyList()
    private var currentDistricts: List<DistrictModel> = emptyList()
    private var currentSubdistricts: List<SubDistrictModel> = emptyList()
    private var currentAreaList: List<AreaModel> = emptyList()
    private var storeOwnerList: MutableList<StoreOwnerModel> = mutableListOf()

    private val multiplePermissionList = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun setAllClickListener() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkMultiplePermission()

        binding.btnAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCustomerFragment)
        }
    }

    override fun allObserver() {
        observeDivisions()
        observeDistricts()
        observeSubDistricts()
        observeAreas()
        observeStoreOwners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        resetSpinner(binding.spinnerDivision, "Select Division")
        resetSpinner(binding.spinnerDistrict, "Select District")
        resetSpinner(binding.spinnerSubdistrict, "Select Subdistrict")
        resetSpinner(binding.spinnerArea, "Select Area")
        disableSpinner(binding.spinnerDistrict)
        disableSpinner(binding.spinnerSubdistrict)
        disableSpinner(binding.spinnerArea)
        divisionViewModel.getDivisions()
    }

    private fun setupRecyclerView() {
        storeOwnerAdapter = StoreOwnerAdapter(emptyList(), this)
        binding.rvAllCustomer.adapter = storeOwnerAdapter
    }

    private fun clearStoreList(showEmptyMessage: Boolean = true) {
        storeOwnerList.clear()
        storeOwnerAdapter = StoreOwnerAdapter(emptyList(), this)
        binding.rvAllCustomer.adapter = storeOwnerAdapter
        binding.rvAllCustomer.visibility = View.GONE
        binding.tvAllCustomer.visibility = View.GONE
        binding.tvNoStoreFound.visibility = if (showEmptyMessage) View.VISIBLE else View.GONE
    }

    private fun observeStoreOwners() {
        viewLifecycleOwner.lifecycleScope.launch {
            storeOwnerViewModel.storeOwnerState.collect { response ->
                if (response.loading) {
                    loading.show()
                }
                if (response.data != null) {
                    loading.dismiss()
                    storeOwnerList = response.data.toMutableList()
                    nearByStoreOwner()
                    Log.d("TAG", "observeStoreOwners: ${response.data} ")
                }
                if (response.error != null) {
                    loading.dismiss()
                    clearStoreList(showEmptyMessage = true)
                }
            }
        }
    }

    private fun filterStoresByLocation(currentLocation: Location) {
        val storesWithDistance =
            storeOwnerList.filter { it.coordinates?.size == 2 }.mapNotNull { store ->
                val lat = store.coordinates?.getOrNull(0)?.toDoubleOrNull()
                val lon = store.coordinates?.getOrNull(1)?.toDoubleOrNull()

                if (lat != null && lon != null) {
                    val storeLocation = Location("").apply {
                        latitude = lat
                        longitude = lon
                    }
                    val distanceInMeters = currentLocation.distanceTo(storeLocation)
                    if (distanceInMeters <= 50) {
                        val roundedDistance = String.format("%.2f", distanceInMeters).toDouble()
                        store.copy(distance = roundedDistance)
                    } else {
                        null // If the store is further than 50 meters, exclude it
                    }
                } else null
            }.sortedBy { it.distance }

        storeOwnerList = storesWithDistance.toMutableList()

        if (storeOwnerList.isNotEmpty()) {
            setStoreOwnerAdapterData()
        } else {
            clearStoreList(showEmptyMessage = true)
        }
    }

    @SuppressLint("MissingPermission", "DefaultLocale")
    private fun nearByStoreOwner() {
        if (storeOwnerList.isEmpty()) {
            clearStoreList(showEmptyMessage = true)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation: Location? ->
            if (!isAdded) return@addOnSuccessListener
            if (currentLocation != null) {
                filterStoresByLocation(currentLocation)
            } else {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { freshLocation: Location? ->
                        if (!isAdded) return@addOnSuccessListener
                        if (freshLocation != null) {
                            filterStoresByLocation(freshLocation)
                        } else {
                            warningPermissionDialog(
                                requireContext(),
                                "Unable to get current location. Please ensure location services are enabled For get Near By Home List"
                            ) { _, _ -> }
                        }
                    }
            }
        }.addOnFailureListener {
            if (!isAdded) return@addOnFailureListener
            Toast.makeText(
                requireContext(),
                "Failed to get location, Please turn on location. ${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setStoreOwnerAdapterData() {
        if (storeOwnerList.isNotEmpty()) {
            storeOwnerAdapter = StoreOwnerAdapter(storeOwnerList, this)
            binding.rvAllCustomer.adapter = storeOwnerAdapter
            binding.tvNoStoreFound.visibility = View.GONE
            binding.rvAllCustomer.visibility = View.VISIBLE
            binding.tvAllCustomer.visibility = View.VISIBLE
        }
    }

    private fun observeDivisions() {
        viewLifecycleOwner.lifecycleScope.launch {
            divisionViewModel.divisionState.collect { response ->
                if (response.loading) return@collect

                val list = if (!response.data.isNullOrEmpty()) {
                    response.data
                } else if (response.error != null) {
                    divisions
                } else {
                    return@collect
                }

                currentDivisions = list
                populateDivisionSpinner(currentDivisions)
            }
        }
    }

    private fun populateDivisionSpinner(list: List<Divisions>) {
        val names = listOf("Select Division") + list.map { it.divisionName }
        binding.spinnerDivision.adapter = CustomSpinnerAdapter(
            requireContext(),
            names,
            setOf("Select Division")
        )
        enableSpinner(binding.spinnerDivision)

        binding.spinnerDivision.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentDistricts = emptyList()
                    currentSubdistricts = emptyList()
                    currentAreaList = emptyList()

                    resetSpinner(binding.spinnerDistrict, "Select District")
                    resetSpinner(binding.spinnerSubdistrict, "Select Subdistrict")
                    resetSpinner(binding.spinnerArea, "Select Area")

                    disableSpinner(binding.spinnerDistrict)
                    disableSpinner(binding.spinnerSubdistrict)
                    disableSpinner(binding.spinnerArea)
                    clearStoreList(showEmptyMessage = false)

                    if (position == 0) return

                    val divisionId = currentDivisions[position - 1].divisionId
                    districtViewModel.getDistrict(divisionId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        if (currentDivisions.isNotEmpty()) {
            binding.spinnerDivision.setSelection(1)
        }
    }

    private fun observeDistricts() {
        viewLifecycleOwner.lifecycleScope.launch {
            districtViewModel.districtState.collect { response ->

                if (response.loading) return@collect

                response.data?.let { list ->

                    currentDistricts = list

                    val names = listOf("Select District") +
                            list.map { it.disName }

                    binding.spinnerDistrict.adapter =
                        CustomSpinnerAdapter(
                            requireContext(),
                            names,
                            setOf("Select District")
                        )

                    if (currentDistricts.isNotEmpty()) {
                        enableSpinner(binding.spinnerDistrict)
                        binding.spinnerDistrict.setSelection(1)
                    } else {
                        disableSpinner(binding.spinnerDistrict)
                        clearStoreList(showEmptyMessage = true)
                    }

                    binding.spinnerDistrict.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                currentSubdistricts = emptyList()
                                currentAreaList = emptyList()

                                resetSpinner(
                                    binding.spinnerSubdistrict,
                                    "Select Subdistrict"
                                )

                                resetSpinner(
                                    binding.spinnerArea,
                                    "Select Area"
                                )

                                disableSpinner(binding.spinnerSubdistrict)
                                disableSpinner(binding.spinnerArea)
                                clearStoreList(showEmptyMessage = false)

                                if (position == 0) return

                                val selectedDistrictId =
                                    currentDistricts[position - 1].disNo

                                subDistrictViewModel.getSubDistrict(
                                    selectedDistrictId
                                )
                            }

                            override fun onNothingSelected(
                                parent: AdapterView<*>?
                            ) {}
                        }
                }

                response.error?.let {
                    clearStoreList(showEmptyMessage = true)
                }
            }
        }
    }

    private fun observeSubDistricts() {
        viewLifecycleOwner.lifecycleScope.launch {
            subDistrictViewModel.subDistrictState.collect { response ->

                if (response.loading) return@collect

                response.data?.let { list ->

                    currentSubdistricts = list

                    val names = listOf("Select Subdistrict") +
                            list.map { it.subDisName }

                    binding.spinnerSubdistrict.adapter =
                        CustomSpinnerAdapter(
                            requireContext(),
                            names,
                            setOf("Select Subdistrict")
                        )

                    if (currentSubdistricts.isNotEmpty()) {
                        enableSpinner(binding.spinnerSubdistrict)
                        binding.spinnerSubdistrict.setSelection(1)
                    } else {
                        disableSpinner(binding.spinnerSubdistrict)
                        clearStoreList(showEmptyMessage = true)
                    }

                    binding.spinnerSubdistrict.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                currentAreaList = emptyList()

                                resetSpinner(
                                    binding.spinnerArea,
                                    "Select Area"
                                )

                                disableSpinner(binding.spinnerArea)
                                clearStoreList(showEmptyMessage = false)

                                if (position == 0) return

                                val selectedSubDistrictId =
                                    currentSubdistricts[position - 1].subDisNo

                                areaViewModel.getArea(selectedSubDistrictId)
                            }

                            override fun onNothingSelected(
                                parent: AdapterView<*>?
                            ) {}
                        }
                }

                response.error?.let {
                    clearStoreList(showEmptyMessage = true)
                }
            }
        }
    }

    private fun observeAreas() {
        viewLifecycleOwner.lifecycleScope.launch {
            areaViewModel.areaState.collect { response ->

                if (response.loading) return@collect

                response.data?.let { list ->

                    currentAreaList = list

                    val names = listOf("Select Area") +
                            list.map { it.areaName }

                    binding.spinnerArea.adapter =
                        CustomSpinnerAdapter(
                            requireContext(),
                            names,
                            setOf("Select Area")
                        )

                    if (currentAreaList.isNotEmpty()) {
                        enableSpinner(binding.spinnerArea)
                        binding.spinnerArea.setSelection(1)
                    } else {
                        disableSpinner(binding.spinnerArea)
                        clearStoreList(showEmptyMessage = true)
                    }

                    binding.spinnerArea.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) {
                                    clearStoreList(showEmptyMessage = false)
                                    return
                                }

                                val selectedAreaId =
                                    currentAreaList[position - 1].areaNo

                                // Fetch store owners using the actual area ID
                                storeOwnerViewModel.getStoreOwnerByArea(
                                    selectedAreaId
                                )
                            }

                            override fun onNothingSelected(
                                parent: AdapterView<*>?
                            ) {}
                        }
                }

                response.error?.let {
                    clearStoreList(showEmptyMessage = true)
                }
            }
        }
    }

    private fun resetSpinner(
        spinner: android.widget.Spinner,
        defaultText: String
    ) {
        val adapter = CustomSpinnerAdapter(
            requireContext(),
            listOf(defaultText),
            setOf(defaultText)
        )

        spinner.adapter = adapter
        spinner.setSelection(0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouch(spinner: View, icon: View) {
        spinner.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                rotateIcon(icon, true)
            }
            false
        }
    }

    private fun setupSpinner(spinner: View, items: List<String>) {
        (spinner as? android.widget.Spinner)?.adapter = CustomSpinnerAdapter(
            requireContext(), items, setOf(items.first())
        )
    }

    private fun rotateIcon(icon: View, expanded: Boolean) {
        icon.animate().rotation(if (expanded) 180f else 0f).setDuration(300).start()
    }

    private fun enableSpinner(spinner: View) {
        spinner.isEnabled = true
        spinner.alpha = 1f
    }

    private fun disableSpinner(spinner: View) {
        spinner.isEnabled = false
        spinner.alpha = 0.5f
    }

    override fun selectCustomer(userId: String, id: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToStoreOwnerDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun editClickListener(storeOwnerModel: StoreOwnerModel) {

    }

    override fun deleteClickListener(storeOwnerModel: StoreOwnerModel) {

    }

    private fun checkMultiplePermission() {
        val needListOfPermission = ArrayList<String>()
        for (permission in multiplePermissionList) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                needListOfPermission.add(permission)
            }
        }
        if (needListOfPermission.isNotEmpty()) {
            requestPermissions(
                needListOfPermission.toTypedArray(), Constants.MULTIPLE_PERMISSIONS_CODE
            )
        } else {
            LocationForegroundService.startService(requireContext())
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        handleMultiplePermissionsResult(
            context = requireContext(),
            activity = requireActivity(),
            requestCode = requestCode,
            expectedRequestCode = Constants.MULTIPLE_PERMISSIONS_CODE,
            permissions = permissions,
            grantResults = grantResults,
            onGranted = {
                // Permissions granted, proceed
                LocationForegroundService.startService(requireContext())
                nearByStoreOwner()
            },
            onDeniedRetry = {
                // Retry permission request
                checkMultiplePermission()
            })
    }

    fun warningPermissionDialog(
        context: Context,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        MaterialAlertDialogBuilder(context)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", listener)
            .create()
            .show()
    }
}
