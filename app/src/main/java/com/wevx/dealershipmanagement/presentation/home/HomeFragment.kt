package com.wevx.dealershipmanagement.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentHomeBinding
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel
import com.wevx.dealershipmanagement.presentation.adapter.CustomSpinnerAdapter
import com.wevx.dealershipmanagement.presentation.adapter.StoreOwnerAdapter
import com.wevx.dealershipmanagement.presentation.home.getArea.AreaViewModel
import com.wevx.dealershipmanagement.presentation.home.getDistrict.DistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByDistrict.StoreOwnerByDistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getSubDistrict.SubDistrictViewModel
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisions
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    StoreOwnerAdapter.HandleCustomerClickListener {

    private val districtViewModel: DistrictViewModel by viewModels()
    private val subDistrictViewModel: SubDistrictViewModel by viewModels()
    private val areaViewModel: AreaViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private val storeByDisViewModel: StoreOwnerByDistrictViewModel by viewModels()


    private lateinit var storeOwnerAdapter: StoreOwnerAdapter

    override fun setAllClickListener() {
        binding.btnAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCustomerFragment)
        }

        storeByDisViewModel.getStoreOwnerByDis(1)
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
        setupTouchListeners()
        setupInitialSpinners()
        setupRecyclerView()
        observeLoading()
        allObserver()
        setAllClickListener()
        setupDivisionSpinner()
    }

    private fun setupRecyclerView() {
        storeOwnerAdapter = StoreOwnerAdapter(emptyList(), this)
        binding.rvAllCustomer.adapter = storeOwnerAdapter
    }

    private fun observeLoading() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
    }

    private fun observeStoreOwners() {
        homeViewModel.storeOwners.observe(viewLifecycleOwner) { list ->
            storeOwnerAdapter.updateData(list)
            binding.tvNoStoreFound.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            binding.rvAllCustomer.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
            binding.tvAllCustomer.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeDistricts() {
        districtViewModel.districtState.collectInLifecycle(viewLifecycleOwner) {
            if (it.loading) return@collectInLifecycle
            it.data?.let { list ->
                val names = list.map { d -> d.disName }
                setupSpinner(binding.spinnerDistrict, listOf("Select District") + names)
                enableSpinner(binding.spinnerDistrict)

                // Restore selection if exists
                if (homeViewModel.selectedDistrictIndex > 0) {
                    binding.spinnerDistrict.setSelection(homeViewModel.selectedDistrictIndex)
                }

                binding.spinnerDistrict.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            homeViewModel.selectedDistrictIndex = position
                            if (position == 0) {
                                resetSpinners("district")
                                return
                            }
                            homeViewModel.selectedDistrictId = list[position - 1].disNo
                            subDistrictViewModel.getSubDistrict(homeViewModel.selectedDistrictId)
                            resetSpinners("subdistrict")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
            }
        }
    }

    private fun observeSubDistricts() {
        subDistrictViewModel.subDistrictState.collectInLifecycle(viewLifecycleOwner) {
            if (it.loading) return@collectInLifecycle
            it.data?.let { list ->
                val names = list.map { s -> s.subDisName }
                setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict") + names)
                enableSpinner(binding.spinnerSubdistrict)

                if (homeViewModel.selectedSubDistrictIndex > 0) {
                    binding.spinnerSubdistrict.setSelection(homeViewModel.selectedSubDistrictIndex)
                }

                binding.spinnerSubdistrict.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            homeViewModel.selectedSubDistrictIndex = position
                            if (position == 0) {
                                resetSpinners("subdistrict")
                                return
                            }
                            homeViewModel.selectedSubDistrictId = list[position - 1].subDisNo
                            areaViewModel.getArea(homeViewModel.selectedSubDistrictId)
                            resetSpinners("area")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
            }
        }
    }

    private fun observeAreas() {
        areaViewModel.areaState.collectInLifecycle(viewLifecycleOwner) {
            if (it.loading) return@collectInLifecycle
            it.data?.let { list ->
                val names = list.map { a -> a.areaName }
                setupSpinner(binding.spinnerArea, listOf("Select Area") + names)
                enableSpinner(binding.spinnerArea)

                if (homeViewModel.selectedAreaIndex > 0) {
                    binding.spinnerArea.setSelection(homeViewModel.selectedAreaIndex)
                }

                binding.spinnerArea.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            homeViewModel.selectedAreaIndex = position
                            if (position == 0) {
                                binding.rvAllCustomer.visibility = View.GONE
                                binding.tvNoStoreFound.visibility = View.GONE
                                return
                            }
                            homeViewModel.selectedAreaId = list[position - 1].areaNo
                            homeViewModel.fetchStoreOwners(homeViewModel.selectedAreaId)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
            }
        }
    }

    private fun setupDivisionSpinner() {
        val divisionNames = listOf("Select Division") + divisions.map { it.divisionName }
        setupSpinner(binding.spinnerDivision, divisionNames)

        if (homeViewModel.selectedDivisionIndex > 0) {
            binding.spinnerDivision.setSelection(homeViewModel.selectedDivisionIndex)
        }

        binding.spinnerDivision.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    rotateIcon(binding.divisionDropdownIcon, false)
                    homeViewModel.selectedDivisionIndex = position

                    if (position == 0) {
                        resetSpinners("division")
                        return
                    }

                    homeViewModel.selectedDivisionId = divisions[position - 1].divisionId
                    districtViewModel.getDistrict(homeViewModel.selectedDivisionId)
                    resetSpinners("district")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupInitialSpinners() {
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

    private fun resetSpinners(from: String) {
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
    }

    override fun selectCustomer(userId: String, id: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToStoreOwnerDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun editClickListener(storeOwnerModel: StoreOwnerModel) {

    }

    override fun deleteClickListener(storeOwnerModel: StoreOwnerModel) {

    }
}
