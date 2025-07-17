package com.wevx.dealershipmanagement.home

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.Log
import com.wevx.dealershipmanagement.CustomSpinnerAdapter
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var selectedCity: String
    private lateinit var selectedArea: String
    private lateinit var selectedZone: String

    override fun setAllClickListener() {

    }

    override fun allObserver() {

    }


    private val cityAreaZoneMap = mapOf(
        "Dhaka" to mapOf(
            "Mirpur" to listOf("Mirpur-1", "Mirpur-2", "Mirpur-10"),
            "Mohammadpur" to listOf("Shyamoli", "Mohammadia Housing", "Town Hall"),
            "Kafrul" to listOf("Shewrapara", "Kazipara", "Taltola")
        ),
        "Chattogram" to mapOf(
            "Agrabad" to listOf("Sector-1", "Sector-2", "Sector-5"),
            "Pahartali" to listOf("Block-A", "Block-B"),
            "Kotwali" to listOf("Zone-1", "Zone-2")
        )
    )


    // Flags to track spinner open/close state
    private var cityExpanded = false
    private var areaExpanded = false
    private var zoneExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCityAreaZoneSpinners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCityAreaZoneSpinners() {
        val cityList = listOf("Select City") + cityAreaZoneMap.keys.toList()
        val cityAdapter = CustomSpinnerAdapter(requireContext(), cityList)
        binding.spinnerCity.adapter = cityAdapter

        binding.spinnerArea.isEnabled = false
        binding.spinnerZone.isEnabled = false

        // Set touch listeners to detect spinner opening
        binding.spinnerCity.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                cityExpanded = true
                updateSpinnerIcon(binding.spinnerCity, cityExpanded)
            }
            false
        }
        binding.spinnerArea.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                areaExpanded = true
                updateSpinnerIcon(binding.spinnerArea, areaExpanded)
            }
            false
        }
        binding.spinnerZone.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                zoneExpanded = true
                updateSpinnerIcon(binding.spinnerZone, zoneExpanded)
            }
            false
        }

        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCity = parent.getItemAtPosition(position).toString()
                Log.d("SelectedItem", "City: $selectedCity")
                cityExpanded = false
                updateSpinnerIcon(binding.spinnerCity, cityExpanded)

                if (position == 0) {
                    // Reset area & zone
                    binding.spinnerArea.adapter = null
                    binding.spinnerZone.adapter = null
                    binding.spinnerArea.isEnabled = false
                    binding.spinnerZone.isEnabled = false
                } else {
                    val selectedCity = cityList[position]
                    val areaMap = cityAreaZoneMap[selectedCity] ?: emptyMap()
                    val areaList = listOf("Select Area") + areaMap.keys

                    val areaAdapter = CustomSpinnerAdapter(requireContext(), areaList)
                    binding.spinnerArea.adapter = areaAdapter
                    binding.spinnerArea.isEnabled = true

                    binding.spinnerZone.adapter = null
                    binding.spinnerZone.isEnabled = false

                    binding.spinnerArea.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                pos: Int,
                                id: Long
                            ) {
                                selectedArea = parent.getItemAtPosition(position).toString()
                                Log.d("SelectedItem", "Area: $selectedArea")
                                areaExpanded = false
                                updateSpinnerIcon(binding.spinnerArea, areaExpanded)

                                if (pos == 0) {
                                    binding.spinnerZone.adapter = null
                                    binding.spinnerZone.isEnabled = false
                                } else {
                                    val selectedArea = areaList[pos]
                                    val zoneList = listOf("Select Zone") + (areaMap[selectedArea]
                                        ?: emptyList())
                                    val zoneAdapter =
                                        CustomSpinnerAdapter(requireContext(), zoneList)
                                    binding.spinnerZone.adapter = zoneAdapter
                                    binding.spinnerZone.isEnabled = true

                                    binding.spinnerZone.onItemSelectedListener =
                                        object : AdapterView.OnItemSelectedListener {
                                            override fun onItemSelected(
                                                parent: AdapterView<*>,
                                                view: View?,
                                                position: Int,
                                                id: Long
                                            ) {
                                                selectedZone =
                                                    parent.getItemAtPosition(position).toString()
                                                Log.d("SelectedItem", "Zone: $selectedZone")
                                                zoneExpanded = false
                                                updateSpinnerIcon(binding.spinnerZone, zoneExpanded)
                                            }

                                            override fun onNothingSelected(parent: AdapterView<*>) {}
                                        }
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {}
                        }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateSpinnerIcon(spinner: Spinner, expanded: Boolean) {
        val selectedView = spinner.selectedView ?: return
        val icon = selectedView.findViewById<ImageView>(R.id.spinner_icon) ?: return

        // Animate rotation from 0 to 180 degrees or back
        val fromRotation = if (expanded) 0f else 180f
        val toRotation = if (expanded) 180f else 0f

        ObjectAnimator.ofFloat(icon, "rotation", fromRotation, toRotation).apply {
            duration = 300
            start()
        }
    }
}
