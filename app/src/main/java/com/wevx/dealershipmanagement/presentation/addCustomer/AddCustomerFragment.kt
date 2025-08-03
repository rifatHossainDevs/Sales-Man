package com.wevx.dealershipmanagement.presentation.addCustomer

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
import com.wevx.dealershipmanagement.utils.areAllPermissionGranted
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentAddCustomerBinding
import com.wevx.dealershipmanagement.utils.LocalDatabase
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisions
import com.wevx.dealershipmanagement.utils.requestPermission

class AddCustomerFragment :
    BaseFragment<FragmentAddCustomerBinding>(FragmentAddCustomerBinding::inflate) {
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
    private var selectedDivisionId = 1
    private var selectedDistrictId = 1
    private var selectedAreaId = 1
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
        allButtonClickListener()
        permissionRequest = getPermissionRequest()

        binding.btnUploadImageStoreOwner.setOnClickListener {
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
                    binding.ivStoreOwner.setImageURI(fileUri)
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

        binding.spinnerDivision.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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
                    val districts =
                        listOf("Select District") + (districtMap[selectedDivision.divisionName]
                            ?: emptyList())

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
        binding.spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    rotateIcon(binding.districtDropdownIcon, false)

                    if (position == 0) {
                        setupSpinner(binding.spinnerSubdistrict, listOf("Select Subdistrict"))
                        setupSpinner(binding.spinnerArea, listOf("Select Area"))
                        disableSpinner(binding.spinnerSubdistrict)
                        disableSpinner(binding.spinnerArea)
                        return
                    }

                    val selectedDistrict = districts[position]
                    val subdistricts =
                        listOf("Select Subdistrict") + (subdistrictMap[selectedDistrict]
                            ?: emptyList())

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
        binding.spinnerSubdistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    rotateIcon(binding.subdistrictDropdownIcon, false)

                    if (position == 0) {
                        setupSpinner(binding.spinnerArea, listOf("Select Area"))
                        disableSpinner(binding.spinnerArea)
                        return
                    }

                    val selectedSubdistrict = subdistricts[position]
                    val areas =
                        listOf("Select Area") + (areaMap[selectedSubdistrict] ?: emptyList())

                    setupSpinner(binding.spinnerArea, areas)
                    enableSpinner(binding.spinnerArea)

                    setupAreaSpinner(areas)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun setupAreaSpinner(areas: List<String>) {
        binding.spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
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