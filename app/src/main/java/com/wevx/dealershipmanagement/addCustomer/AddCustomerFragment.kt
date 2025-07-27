package com.wevx.dealershipmanagement.addCustomer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.areAllPermissionGranted
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentAddCustomerBinding
import com.wevx.dealershipmanagement.local_database.LocalDatabase
import com.wevx.dealershipmanagement.requestPermission

class AddCustomerFragment :
    BaseFragment<FragmentAddCustomerBinding>(FragmentAddCustomerBinding::inflate) {
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>

    override fun setAllClickListener() {
        allButtonClickListener()
        permissionRequest = getPermissionRequest()

        binding.btnUploadImage.setOnClickListener {
            requestPermission(permissionRequest, permissionList)
        }
    }

    override fun allObserver() {

    }

    private fun getPermissionRequest(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (areAllPermissionGranted(permissionList)) {
                ImagePicker.Companion.with(this)
                    .cropSquare()
                    .cameraOnly()
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        512,
                        512
                    )  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

        private val permissionList = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                //viewmodel.setImageUri(fileUri)
                //product.imageLink = fileUri.toString()
                if (fileUri.toString() != "") {
                    binding.ivUser.setImageURI(fileUri)
                }

            } else if (resultCode == ImagePicker.Companion.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    fun allButtonClickListener() {
        binding.btnCreateCustomer.setOnClickListener {
            findNavController().navigate(R.id.action_addCustomerFragment_to_homeFragment)
        }
    }


    //spinner code
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
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            LocalDatabase.cityList
        )
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

                val selectedCity = LocalDatabase.cityList[position]
                if (selectedCity == "Select City") {
                    binding.spinnerArea.isEnabled = false
                    binding.spinnerZone.isEnabled = false
                    binding.spinnerArea.alpha = 0.5f
                    binding.spinnerZone.alpha = 0.5f
                } else {
                    val areas = LocalDatabase.areaMap[selectedCity] ?: listOf("Select Area")
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
                    val zones = LocalDatabase.zoneMap[selectedArea] ?: listOf("Select Zone")
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

}