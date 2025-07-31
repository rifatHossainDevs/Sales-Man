package com.wevx.dealershipmanagement.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentHomeBinding
import com.wevx.dealershipmanagement.domain.models.Customers
import com.wevx.dealershipmanagement.presentation.adapter.CustomerAdapter
import com.wevx.dealershipmanagement.presentation.home.getArea.AreaViewModel
import com.wevx.dealershipmanagement.presentation.home.getDistrict.DistrictDataState
import com.wevx.dealershipmanagement.presentation.home.getDistrict.DistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getSubDistrict.SubDistrictViewModel
import com.wevx.dealershipmanagement.utils.LocalDatabase.customers
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisions
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    CustomerAdapter.HandleCustomerClickListener {

    private lateinit var adapter: CustomerAdapter
    private var selectedDivisionId = 1
    private var selectedDistrictId = 1
    private var selectedAreaId = 1
    private val districtViewModel: DistrictViewModel by viewModels()
    private val subDistrictViewModel: SubDistrictViewModel by viewModels()
    private val areaViewModel: AreaViewModel by viewModels()

    private val districtMap = mapOf(
        "Dhaka" to listOf("Gazipur", "Narayanganj"),
        "Chattogram" to listOf("Cox's Bazar", "Rangamati")
    )

    private val subdistrictMap = mapOf(
        "Gazipur" to listOf("Tongi", "Sreepur"),
        "Narayanganj" to listOf("Sonargaon", "Rupganj")
    )

    private val areaMap = mapOf(
        "Tongi" to listOf("Tongi Area 1", "Tongi Area 2"),
        "Sreepur" to listOf("Sreepur Area 1")
    )

    override fun setAllClickListener() {
        setRecyclerView()
        binding.btnAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCustomerFragment)
        }

        districtViewModel.getDistrict(selectedDivisionId)
        subDistrictViewModel.getSubDistrict(selectedDistrictId)
        areaViewModel.getArea(selectedAreaId)
    }

    override fun allObserver() {
        districtObserver()
        subDistrictObserver()
        areaObserver()
    }

    private fun areaObserver() {

    }

    private fun subDistrictObserver() {

    }

    private fun districtObserver() {
        districtViewModel.districtState.collectInLifecycle(viewLifecycleOwner) { districtState ->
            if (districtState.loading) return@collectInLifecycle

            districtState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            districtState.data?.let {districtList ->

            }
        }
    }

    private fun setRecyclerView() {
        adapter = CustomerAdapter(customers, this)
        binding.rvAllCustomer.adapter = adapter
    }

    override fun selectCustomer(customerId: String) {
        findNavController().navigate(R.id.action_homeFragment_to_productsFragment)
    }

    override fun editClickListener(customer: Customers) {}
    override fun deleteClickListener(customers: Customers) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTouchListeners()

        setupDivisionSpinner()
        setupInitialSpinners()
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
        binding.spinnerDivision.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                rotateIcon(binding.divisionDropdownIcon, true)
            false
        }
        binding.spinnerDistrict.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                rotateIcon(binding.districtDropdownIcon, true)
            false
        }
        binding.spinnerSubdistrict.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                rotateIcon(binding.subdistrictDropdownIcon, true)
            false
        }
        binding.spinnerArea.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                rotateIcon(binding.areaDropdownIcon, true)
            false
        }
    }

    private fun setupDivisionSpinner() {
        val divisionNames = listOf("Select Division") + divisions.map { it.divisionName }

        setupSpinner(binding.spinnerDivision, divisionNames)

        binding.spinnerDivision.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                rotateIcon(binding.divisionDropdownIcon, false)

                if (position == 0) {
                    setupSpinner(binding.spinnerDistrict, listOf("Select District"))
                    setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict"))
                    setupSpinner(binding.spinnerArea, listOf("Select Area"))
                    disableSpinner(binding.spinnerDistrict)
                    disableSpinner(binding.spinnerSubdistrict)
                    disableSpinner(binding.spinnerArea)
                    return
                }

                val selectedDivision = divisions[position - 1]
                selectedDivisionId = selectedDivision.divisionId
                val districts = listOf("Select District") + (districtMap[selectedDivision.divisionName] ?: emptyList())

                setupSpinner(binding.spinnerDistrict, districts)
                enableSpinner(binding.spinnerDistrict)

                setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict"))
                setupSpinner(binding.spinnerArea, listOf("Select Area"))
                disableSpinner(binding.spinnerSubdistrict)
                disableSpinner(binding.spinnerArea)

                setupDistrictSpinner(districts)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupDistrictSpinner(districts: List<String>) {
        binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                rotateIcon(binding.districtDropdownIcon, false)

                if (position == 0) {
                    setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict"))
                    setupSpinner(binding.spinnerArea, listOf("Select Area"))
                    disableSpinner(binding.spinnerSubdistrict)
                    disableSpinner(binding.spinnerArea)
                    return
                }

                val selectedDistrict = districts[position]
                val subdistricts = listOf("Select Subdistrict") + (subdistrictMap[selectedDistrict] ?: emptyList())

                setupSpinner(binding.spinnerSubdistrict, subdistricts)
                enableSpinner(binding.spinnerSubdistrict)

                setupSpinner(binding.spinnerArea, listOf("Select Area"))
                disableSpinner(binding.spinnerArea)

                setupSubdistrictSpinner(subdistricts)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSubdistrictSpinner(subdistricts: List<String>) {
        binding.spinnerSubdistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                rotateIcon(binding.subdistrictDropdownIcon, false)

                if (position == 0) {
                    setupSpinner(binding.spinnerArea, listOf("Select Area"))
                    disableSpinner(binding.spinnerArea)
                    return
                }

                val selectedSubdistrict = subdistricts[position]
                val areas = listOf("Select Area") + (areaMap[selectedSubdistrict] ?: emptyList())

                setupSpinner(binding.spinnerArea, areas)
                enableSpinner(binding.spinnerArea)

                setupAreaSpinner(areas)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupAreaSpinner(areas: List<String>) {
        binding.spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                rotateIcon(binding.areaDropdownIcon, false)
                // Handle selected area here
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSpinner(spinner: View, items: List<String>) {
        (spinner as? android.widget.Spinner)?.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
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
}
