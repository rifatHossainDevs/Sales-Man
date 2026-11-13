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
import com.wevx.dealershipmanagement.domain.models.DistrictModel
import com.wevx.dealershipmanagement.domain.models.SubDistrictModel
import com.wevx.dealershipmanagement.domain.models.AreaModel
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentHomeBinding
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel
import com.wevx.dealershipmanagement.presentation.adapter.CustomSpinnerAdapter
import com.wevx.dealershipmanagement.presentation.adapter.StoreOwnerAdapter
import com.wevx.dealershipmanagement.presentation.home.getArea.AreaViewModel
import com.wevx.dealershipmanagement.presentation.home.getDistrict.DistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByArea.StoreOwnerViewModel
import com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByDistrict.StoreOwnerByDistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getSubDistrict.SubDistrictViewModel
import com.wevx.dealershipmanagement.utils.Constants
import com.wevx.dealershipmanagement.utils.LocalDatabase
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisionList
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisions
import com.wevx.dealershipmanagement.utils.handleMultiplePermissionsResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    StoreOwnerAdapter.HandleCustomerClickListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val districtViewModel: DistrictViewModel by viewModels()
    private val subDistrictViewModel: SubDistrictViewModel by viewModels()
    private val areaViewModel: AreaViewModel by viewModels()
    private val storeOwnerViewModel: StoreOwnerViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val storeByDisViewModel: StoreOwnerByDistrictViewModel by viewModels()


    private lateinit var storeOwnerAdapter: StoreOwnerAdapter

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

        //storeByDisViewModel.getStoreOwnerByDis(1)

        /*districtViewModel.getDistrict(1)
        subDistrictViewModel.getSubDistrict(1)
        areaViewModel.getArea(1)*/
        //storeOwnerViewModel.getStoreOwnerByArea(2)
    }

    override fun allObserver() {
        observeDistricts()
        observeSubDistricts()
        observeAreas()
        observeStoreOwners()
        //observeStoreOwnersBySubDis()
    }

    /*private fun observeStoreOwnersBySubDis() {
        storeByDisViewModel.storeOwnerByDisState.collectInLifecycle(viewLifecycleOwner){storeOwnerByDisState->
            if (storeOwnerByDisState.loading) return@collectInLifecycle
            storeOwnerByDisState.data?.let { storeOwnerBySubDisList ->

            }
            storeOwnerByDisState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

        }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupTouchListeners()
        //setupInitialSpinners()
        setupRecyclerView()
        //observeLoading()
        allObserver()
        setAllClickListener()
        setDivisionSpinner()
    }

    private fun setupRecyclerView() {
        storeOwnerAdapter = StoreOwnerAdapter(emptyList(), this)
        binding.rvAllCustomer.adapter = storeOwnerAdapter
    }

    /*private fun observeLoading() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
    }*/

    private fun observeStoreOwners() {
        lifecycleScope.launch {
            storeOwnerViewModel.storeOwnerState.collect { response ->
                if (response.loading) {
                    loading.show()
                }
                if (response.data != null) {
                    loading.dismiss()
                    //storeOwnerAdapter.updateData(response.data)
                    storeOwnerList = response.data as MutableList<StoreOwnerModel>
                    //setStoreOwnerAdapterData()
                    nearByStoreOwner()
                    Log.d("TAG", "observeStoreOwners: ${response.data} ")
                }
                if (response.error != null) {
                    loading.dismiss()
                    storeOwnerList.clear()
                    binding.tvNoStoreFound.visibility = View.VISIBLE
                    binding.rvAllCustomer.visibility = View.GONE
                    binding.tvAllCustomer.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("MissingPermission", "DefaultLocale")
    private fun nearByStoreOwner() {
        if (storeOwnerList.isNotEmpty()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation: Location? ->
                if (currentLocation != null) {
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
                        binding.tvNoStoreFound.visibility = View.GONE
                        binding.rvAllCustomer.visibility = View.VISIBLE
                        binding.tvAllCustomer.visibility = View.VISIBLE
                    } else {
                        storeOwnerList.clear()
                        storeOwnerAdapter = StoreOwnerAdapter(storeOwnerList, this)
                        binding.rvAllCustomer.adapter = storeOwnerAdapter
                        binding.tvNoStoreFound.visibility = View.VISIBLE
                        binding.rvAllCustomer.visibility = View.GONE
                        binding.tvAllCustomer.visibility = View.GONE
                    }
                } else {
                    warningPermissionDialog(
                        requireContext(),
                        "Unable to get current location. Please ensure location services are enabled For get Near By Home List"
                    ) { _, _ -> }

                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failed to get location, Please turn on location. ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

    private fun setDivisionSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            divisions.map { it.divisionName }
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerDivision.adapter = adapter

        binding.spinnerDivision.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val divisionId = divisions[position].divisionId

                    currentDistricts = emptyList()
                    currentSubdistricts = emptyList()
                    currentAreaList = emptyList()

                    resetSpinner(
                        binding.spinnerDistrict,
                        "Select District"
                    )
                    resetSpinner(
                        binding.spinnerSubdistrict,
                        "Select Subdistrict"
                    )
                    resetSpinner(
                        binding.spinnerArea,
                        "Select Area"
                    )

                    disableSpinner(binding.spinnerDistrict)
                    disableSpinner(binding.spinnerSubdistrict)
                    disableSpinner(binding.spinnerArea)

                    districtViewModel.getDistrict(divisionId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        if (divisions.isNotEmpty()) {
            binding.spinnerDivision.setSelection(0)
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

                    enableSpinner(binding.spinnerDistrict)

                    if (currentDistricts.isNotEmpty()) {
                        binding.spinnerDistrict.setSelection(1)
                    }

                    binding.spinnerDistrict.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) return

                                val selectedDistrictId =
                                    currentDistricts[position - 1].disNo

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
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setDistrictSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currentDistricts.map { it.disName }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDistrict.adapter = adapter

        binding.spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val districtId = currentDistricts[position].disNo

                    currentSubdistricts = emptyList()
                    currentAreaList = emptyList()

                    subDistrictViewModel.getSubDistrict(districtId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        if (currentDistricts.isNotEmpty()) {
            binding.spinnerDistrict.setSelection(0)
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

                    enableSpinner(binding.spinnerSubdistrict)

                    if (currentSubdistricts.isNotEmpty()) {
                        binding.spinnerSubdistrict.setSelection(1)
                    }

                    binding.spinnerSubdistrict.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) return

                                val selectedSubDistrictId =
                                    currentSubdistricts[position - 1].subDisNo

                                currentAreaList = emptyList()

                                resetSpinner(
                                    binding.spinnerArea,
                                    "Select Area"
                                )

                                disableSpinner(binding.spinnerArea)

                                areaViewModel.getArea(selectedSubDistrictId)
                            }

                            override fun onNothingSelected(
                                parent: AdapterView<*>?
                            ) {}
                        }
                }

                response.error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setSubDistrictSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currentSubdistricts.map { it.subDisName }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSubdistrict.adapter = adapter

        binding.spinnerSubdistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val subDistrictId = currentSubdistricts[position].subDisNo

                    currentAreaList = emptyList()
                    areaViewModel.getArea(subDistrictId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        if (currentSubdistricts.isNotEmpty()) {
            binding.spinnerSubdistrict.setSelection(0)
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

                    enableSpinner(binding.spinnerArea)
                    if (currentAreaList.isNotEmpty()) {
                        binding.spinnerArea.setSelection(1)
                    }

                    binding.spinnerArea.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position == 0) return

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
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setAreaSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currentAreaList.map { it.areaName }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerArea.adapter = adapter

        binding.spinnerArea.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val areaId = currentAreaList[position].areaNo
                    storeOwnerViewModel.getStoreOwnerByArea(areaId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        if (currentAreaList.isNotEmpty()) {
            binding.spinnerArea.setSelection(0)
        }
    }

    private fun resetSpinner(
        spinner: android.widget.Spinner,
        defaultText: String
    ) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(defaultText)
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
        spinner.setSelection(0)
    }

    /*private fun setupInitialSpinners() {
        setupSpinner(binding.spinnerDistrict, listOf("Select District"))
        setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict"))
        setupSpinner(binding.spinnerArea, listOf("Select Area"))

        disableSpinner(binding.spinnerDistrict)
        disableSpinner(binding.spinnerSubdistrict)
        disableSpinner(binding.spinnerArea)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListeners() {
        setupTouch(binding.spinnerDivision, binding.divisionDropdownIcon)
        setupTouch(binding.spinnerDistrict, binding.districtDropdownIcon)
        setupTouch(binding.spinnerSubdistrict, binding.subdistrictDropdownIcon)
        setupTouch(binding.spinnerArea, binding.areaDropdownIcon)
    }*/

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

    /*private fun resetSpinners(from: String) {
        when (from) {
            "division" -> {
                homeViewModel.selectedDistrictIndex = 0
                homeViewModel.selectedSubDistrictIndex = 0
                homeViewModel.selectedAreaIndex = 0

                setupSpinner(binding.spinnerDistrict, listOf("Select District"))
                setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict"))
                setupSpinner(binding.spinnerArea, listOf("Select Area"))
                disableSpinner(binding.spinnerDistrict)
                disableSpinner(binding.spinnerSubdistrict)
                disableSpinner(binding.spinnerArea)
            }

            "district" -> {
                homeViewModel.selectedSubDistrictIndex = 0
                homeViewModel.selectedAreaIndex = 0

                setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict"))
                setupSpinner(binding.spinnerArea, listOf("Select Area"))
                disableSpinner(binding.spinnerSubdistrict)
                disableSpinner(binding.spinnerArea)
            }

            "subdistrict" -> {
                homeViewModel.selectedAreaIndex = 0

                setupSpinner(binding.spinnerArea, listOf("Select Area"))
                disableSpinner(binding.spinnerArea)
            }
        }
    }*/

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
