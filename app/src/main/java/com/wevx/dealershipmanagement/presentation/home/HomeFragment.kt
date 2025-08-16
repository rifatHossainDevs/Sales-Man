package com.wevx.dealershipmanagement.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisionList
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisions
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    StoreOwnerAdapter.HandleCustomerClickListener {

    private val districtViewModel: DistrictViewModel by viewModels()
    private val subDistrictViewModel: SubDistrictViewModel by viewModels()
    private val areaViewModel: AreaViewModel by viewModels()
    private val storeOwnerViewModel: StoreOwnerViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private val storeByDisViewModel: StoreOwnerByDistrictViewModel by viewModels()


    private lateinit var storeOwnerAdapter: StoreOwnerAdapter

    private var districtList: MutableList<String> = mutableListOf()
    private var subDistrictList: MutableList<String> = mutableListOf()
    private var areaList: MutableList<String> = mutableListOf()


    override fun setAllClickListener() {
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
        observeStoreOwnersBySubDis()
    }

    private fun observeStoreOwnersBySubDis() {
        storeByDisViewModel.storeOwnerByDisState.collectInLifecycle(viewLifecycleOwner){storeOwnerByDisState->
            if (storeOwnerByDisState.loading) return@collectInLifecycle
            storeOwnerByDisState.data?.let { storeOwnerBySubDisList ->

            }
            storeOwnerByDisState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

        }
    }

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
                    storeOwnerAdapter.updateData(response.data)
                    binding.tvNoStoreFound.visibility = if (response.data.isEmpty()) View.VISIBLE else View.GONE
                    binding.rvAllCustomer.visibility = if (response.data.isEmpty()) View.GONE else View.VISIBLE
                    binding.tvAllCustomer.visibility = if (response.data.isEmpty()) View.GONE else View.VISIBLE
                }
                if (response.error != null) {
                    loading.dismiss()
                    Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setDivisionSpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, divisionList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDivision.adapter = adapter
        binding.spinnerDivision.setSelection(0)

        binding.spinnerDivision.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                districtViewModel.getDistrict(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun observeDistricts() {
        lifecycleScope.launch {
            districtViewModel.districtState.collect { response->
                if (response.loading) {
                    //loading.show()
                }
                if (response.data != null) {
                    loading.dismiss()
                    districtList.clear()
                    districtList = response.data.map { it?.disName ?: "District" } as MutableList<String>
                    setDistrictSpinner()
                }
                if (response.error != null) {
                    loading.dismiss()
                    Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setDistrictSpinner() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, districtList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDistrict.adapter = adapter
        binding.spinnerDistrict.setSelection(0)

        binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    subDistrictViewModel.getSubDistrict(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun observeSubDistricts() {
        lifecycleScope.launch {
            subDistrictViewModel.subDistrictState.collect { response ->
                if (response.loading) {
                    //loading.show()
                }
                if (response.data != null) {
                    loading.dismiss()
                    subDistrictList.clear()
                    subDistrictList = response.data.map { it?.subDisName ?: "Sub-District" } as MutableList<String>
                    setSubDistrictSpinner()
                }
                if (response.error != null) {
                    loading.dismiss()
                    Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setSubDistrictSpinner() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subDistrictList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSubdistrict.adapter = adapter
        binding.spinnerSubdistrict.setSelection(0)

        binding.spinnerSubdistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    areaViewModel.getArea(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun observeAreas() {
        lifecycleScope.launch {
            areaViewModel.areaState.collect { response->
                if (response.loading) {
                    //loading.show()
                }
                if (response.data != null) {
                    loading.dismiss()
                    areaList.clear()
                    areaList = response.data.map { it?.areaName ?: "Area" } as MutableList<String>
                    setAreaSpinner()
                }
                if (response.error != null) {
                    loading.dismiss()
                    Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setAreaSpinner() {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areaList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerArea.adapter = adapter
        binding.spinnerArea.setSelection(0)

        binding.spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    storeOwnerViewModel.getStoreOwnerByArea(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
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
}
