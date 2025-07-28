package com.wevx.dealershipmanagement.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.utils.LocalDatabase.areaMap
import com.wevx.dealershipmanagement.utils.LocalDatabase.cityList
import com.wevx.dealershipmanagement.utils.LocalDatabase.customers
import com.wevx.dealershipmanagement.utils.LocalDatabase.zoneMap
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentHomeBinding
import com.wevx.dealershipmanagement.domain.models.Customers
import com.wevx.dealershipmanagement.presentation.adapter.CustomerAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    CustomerAdapter.HandleCustomerClickListener {

    private lateinit var adapter: CustomerAdapter

    override fun setAllClickListener() {
        setRecyclerView()
        addCustomerClickListener()


    }


    override fun allObserver() {

    }

    private fun setRecyclerView() {
        adapter = CustomerAdapter(customers, this)
        binding.rvAllCustomer.adapter = adapter
    }

    private fun addCustomerClickListener() {
        binding.btnAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCustomerFragment)
        }
    }


    override fun selectCustomer(customerId: String) {
        findNavController().navigate(R.id.action_homeFragment_to_productsFragment)
    }

    override fun editClickListener(customer: Customers) {

    }

    override fun deleteClickListener(customers: Customers) {

    }


    //Spinner code start
    // Track expanded state per spinner
    private var cityExpanded = false
    private var areaExpanded = false
    private var zoneExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCitySpinner()
        setupAreaSpinner()
        setupZoneSpinner()
        setupSpinnerTouchListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSpinnerTouchListeners() {
        // When spinner touched (opened), rotate arrow up and set expanded true
        binding.spinnerCity.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                cityExpanded = true
                rotateIcon(binding.cityDropdownIcon, true)
            }
            false
        }
        binding.spinnerArea.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                areaExpanded = true
                rotateIcon(binding.areaDropdownIcon, true)
            }
            false
        }
        binding.spinnerZone.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                zoneExpanded = true
                rotateIcon(binding.zoneDropdownIcon, true)
            }
            false
        }
    }

    private fun setupCitySpinner() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCity.adapter = adapter

        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Spinner closed, rotate arrow down and reset expanded state
                cityExpanded = false
                rotateIcon(binding.cityDropdownIcon, false)

                val selectedCity = cityList[position]
                if (selectedCity == "Select City") {
                    binding.spinnerArea.isEnabled = false
                    binding.spinnerZone.isEnabled = false
                    binding.spinnerArea.alpha = 0.5f
                    binding.spinnerZone.alpha = 0.5f
                } else {
                    val areas = areaMap[selectedCity] ?: listOf("Select Area")
                    setupAreaAdapter(areas)
                    binding.spinnerArea.isEnabled = true
                    binding.spinnerZone.isEnabled = false
                    binding.spinnerArea.alpha = 1f
                    binding.spinnerZone.alpha = 0.5f
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupAreaAdapter(areas: List<String>) {
        val areaAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areas)
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerArea.adapter = areaAdapter
    }

    private fun setupAreaSpinner() {
        binding.spinnerArea.isEnabled = false
        val defaultAreaList = listOf("Select Area")
        setupAreaAdapter(defaultAreaList)

        binding.spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                areaExpanded = false
                rotateIcon(binding.areaDropdownIcon, false)

                val selectedArea = binding.spinnerArea.selectedItem.toString()
                if (selectedArea == "Select Area") {
                    binding.spinnerZone.isEnabled = false
                    binding.spinnerZone.alpha = 0.5f
                } else {
                    val zones = zoneMap[selectedArea] ?: listOf("Select Zone")
                    setupZoneAdapter(zones)
                    binding.spinnerZone.isEnabled = true
                    binding.spinnerZone.alpha = 1f
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupZoneAdapter(zones: List<String>) {
        val zoneAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, zones)
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerZone.adapter = zoneAdapter
    }

    private fun setupZoneSpinner() {
        binding.spinnerZone.isEnabled = false
        val defaultZoneList = listOf("Select Zone")
        setupZoneAdapter(defaultZoneList)

        binding.spinnerZone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                zoneExpanded = false
                rotateIcon(binding.zoneDropdownIcon, false)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun rotateIcon(icon: View, expanded: Boolean) {
        val fromDegrees = if (expanded) 0f else 180f
        val toDegrees = if (expanded) 180f else 0f

        icon.animate()
            .rotation(toDegrees)
            .setDuration(300)
            .start()
    }
//spinner code end

}
