package com.wevx.dealershipmanagement.presentation.addCustomer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.utils.areAllPermissionGranted
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentAddCustomerBinding
import com.wevx.dealershipmanagement.domain.models.DistrictModel
import com.wevx.dealershipmanagement.domain.models.SubDistrictModel
import com.wevx.dealershipmanagement.presentation.adapter.CustomSpinnerAdapter
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.presentation.createStoreOwner.CreateStoreViewModel
import com.wevx.dealershipmanagement.presentation.home.getArea.AreaViewModel
import com.wevx.dealershipmanagement.presentation.home.getDistrict.DistrictViewModel
import com.wevx.dealershipmanagement.presentation.home.getSubDistrict.SubDistrictViewModel
import com.wevx.dealershipmanagement.utils.LocalDatabase.divisions
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wevx.dealershipmanagement.domain.models.AreaModel
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.extract
import java.io.File

@AndroidEntryPoint
class AddCustomerFragment :
    BaseFragment<FragmentAddCustomerBinding>(FragmentAddCustomerBinding::inflate) {

    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private val districtViewModel: DistrictViewModel by viewModels()
    private val subDistrictViewModel: SubDistrictViewModel by viewModels()
    private val areaViewModel: AreaViewModel by viewModels()
    private val createStoreViewModel: CreateStoreViewModel by viewModels()
    private val currentUserViewModel: GetProfileViewModel by viewModels()
    private var selectedDivisionId: Int? = null
    private var selectedDistrictId: Int? = null
    private var selectedSubDistrictId: Int? = null
    private var selectedAreaId: Int? = null
    private var storeOwnerImageUri: Uri? = null
    private var storeImageUri: Uri? = null
    lateinit var currentUser: String
    lateinit var token: String
    lateinit var tokenManager: TokenManager

    var hasStoreUri: Boolean = false
    var hasStoreOwnerUri: Boolean = false

    private enum class ImageType {
        STORE_OWNER, STORE
    }

    private var currentImageType: ImageType? = null
    private var currentDistricts: List<DistrictModel> = emptyList()
    private var currentSubdistricts: List<SubDistrictModel> = emptyList()
    private var currentAreaList: List<AreaModel> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        permissionRequest = getPermissionRequest()

        permissionRequest.launch(permissionList)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setAllClickListener() {
        toggleSpinners()

        setupDivisionSpinner()
        allButtonClickListener()
        tokenManager = TokenManager(requireContext())
        token = tokenManager.getAccessToken().toString()

        currentUserViewModel.getProfile(token)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun toggleSpinners() {
        val spinners = listOf(
            binding.spinnerDivision,
            binding.spinnerDistrict,
            binding.spinnerSubdistrict,
            binding.spinnerArea
        )

        if (hasStoreUri && hasStoreOwnerUri) {
            spinners.forEach {
                it.isEnabled = true
                it.setOnTouchListener(null)
                it.alpha = 1f
            }
        } else {
            val toastMessage = "First set images"
            val blockTouchListener = View.OnTouchListener { _, _ ->
                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                true
            }

            spinners.forEach {
                it.isEnabled = false
                it.alpha = 0.5f
                it.setOnTouchListener(blockTouchListener)
            }
        }
    }


    override fun allObserver() {
        observeDistricts()
        observeSubDistricts()
        observeAreas()
        createStoreObserver()
        currentUserObserver()
    }


    private fun currentUserObserver() {
        currentUserViewModel.profileState.collectInLifecycle(viewLifecycleOwner) { profileState ->
            if (profileState.loading) return@collectInLifecycle
            profileState.error?.let {

            }
            profileState.data?.let { user ->
                currentUser = user.id
            }
        }
    }

    fun allButtonClickListener() {
        binding.btnCreateCustomer.setOnClickListener {
            binding.apply {
                val name = etUserName.extract()
                val phone = etPhoneNumber.extract()
                val storeName = etStoreName.extract()
                val storeAddress = etStoreAddress.extract()

                val subDistrictIdToSend =
                    currentSubdistricts[spinnerSubdistrict.selectedItemPosition - 1].subDisNo

                val areaIdToSend = currentAreaList[spinnerArea.selectedItemPosition - 1].areaNo

                val storeImageFile = storeImageUri?.let { uriToFile(it, requireContext()) }
                val storeOwnerImageFile =
                    storeOwnerImageUri?.let { uriToFile(it, requireContext()) }
                /*Log.d(
                    "FileCheck",
                    "storeImageFile: ${storeImageFile?.absolutePath} exists=${storeImageFile?.exists()} size=${storeImageFile?.length()}"
                )*/
                if (storeImageFile == null || storeOwnerImageFile == null) {
                    Toast.makeText(requireContext(), "Please select images", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                val bearerToken = "Bearer ${tokenManager.getAccessToken()}"

                Log.d("TAG", "areaIdToSend: ${areaIdToSend}")
                Log.d("TAG", "subDistrictIdToSend: ${subDistrictIdToSend}")
                createStoreViewModel.createStore(
                    currentUser,
                    storeName,
                    storeImageFile,
                    currentLatitude.toString(),
                    currentLongitude.toString(),
                    areaIdToSend.toString(),
                    storeAddress,
                    name,
                    phone,
                    storeOwnerImageFile,
                    subDistrictIdToSend.toString(),
                    bearerToken
                )


            }

        }

        permissionRequest = getPermissionRequest()

        binding.btnUploadImageStoreOwner.setOnClickListener {
            currentImageType = ImageType.STORE_OWNER
            if (areAllPermissionGranted(permissionList)) {
                launchImagePicker()
            } else {
                permissionRequest.launch(permissionList)
            }
        }

        binding.btnUploadImageStore.setOnClickListener {
            currentImageType = ImageType.STORE
            if (areAllPermissionGranted(permissionList)) {
                launchImagePicker()
            } else {
                permissionRequest.launch(permissionList)
            }
        }
    }


    private fun createStoreObserver() {
        createStoreViewModel.createStoreState.collectInLifecycle(viewLifecycleOwner) { createStoreState ->
            if (createStoreState.loading) {
                loading.show()
                return@collectInLifecycle
            }
            createStoreState.error?.let {
                loading.dismiss()

            }

            createStoreState.data?.let {
                loading.dismiss()
                Toast.makeText(requireContext(), "Store Created Successfully", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_addCustomerFragment_to_homeFragment)
                if (it.status) {
                    Toast.makeText(
                        requireContext(), "Store Created Successfully", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupDivisionSpinner() {
        val divisionNames = listOf("Select Division") + divisions.map { it.divisionName }
        binding.spinnerDivision.adapter =
            CustomSpinnerAdapter(requireContext(), divisionNames, setOf("Select Division"))

        if (divisions.isNotEmpty()) {
            binding.spinnerDivision.setSelection(1)
            selectedDivisionId = divisions[0].divisionId
            districtViewModel.getDistrict(selectedDivisionId!!)
        }

        binding.spinnerDivision.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (position == 0) {
                        resetSpinner(binding.spinnerDistrict, "Select District")
                        resetSpinner(binding.spinnerSubdistrict, "Select Subdistrict")
                        resetSpinner(binding.spinnerArea, "Select Area")
                        return
                    }

                    val selectedDivisionId = divisions[position - 1].divisionId

                    resetSpinner(binding.spinnerDistrict, "Select District")
                    resetSpinner(binding.spinnerSubdistrict, "Select Subdistrict")
                    resetSpinner(binding.spinnerArea, "Select Area")

                    disableSpinner(binding.spinnerDistrict)
                    disableSpinner(binding.spinnerSubdistrict)
                    disableSpinner(binding.spinnerArea)

                    districtViewModel.getDistrict(selectedDivisionId)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun observeDistricts() {
        districtViewModel.districtState.collectInLifecycle(viewLifecycleOwner) {
            if (it.loading) return@collectInLifecycle
            it.data?.let { list ->
                currentDistricts = list
                val names = listOf("Select District") + list.map { d -> d.disName }
                binding.spinnerDistrict.adapter =
                    CustomSpinnerAdapter(requireContext(), names, setOf("Select District"))

                enableSpinner(binding.spinnerDistrict)

                if (list.isNotEmpty()) {
                    binding.spinnerDistrict.setSelection(1)
                    selectedDistrictId = list[0].disNo
                    subDistrictViewModel.getSubDistrict(selectedDistrictId!!)
                }

                binding.spinnerDistrict.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int, id: Long
                        ) {
                            if (position == 0) {
                                resetSpinner(binding.spinnerSubdistrict, "Select Subdistrict")
                                resetSpinner(binding.spinnerArea, "Select Area")
                                return
                            }

                            val selectedDistrictId = currentDistricts[position - 1].disNo

                            resetSpinner(binding.spinnerSubdistrict, "Select Subdistrict")
                            resetSpinner(binding.spinnerArea, "Select Area")

                            disableSpinner(binding.spinnerSubdistrict)
                            disableSpinner(binding.spinnerArea)

                            subDistrictViewModel.getSubDistrict(selectedDistrictId)
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
                currentSubdistricts = list
                val names = listOf("Select Subdistrict") + list.map { s -> s.subDisName }
                binding.spinnerSubdistrict.adapter =
                    CustomSpinnerAdapter(requireContext(), names, setOf("Select Subdistrict"))


                enableSpinner(binding.spinnerSubdistrict)

                if (list.isNotEmpty()) {
                    binding.spinnerSubdistrict.setSelection(1)
                    selectedSubDistrictId = list[0].subDisNo
                    areaViewModel.getArea(selectedSubDistrictId!!)


                }

                binding.spinnerSubdistrict.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int, id: Long
                        ) {
                            if (position == 0) {
                                resetSpinner(binding.spinnerArea, "Select Area")
                                return
                            }

                            val selectedSubdistrictId = currentSubdistricts[position - 1].subDisNo

                            resetSpinner(binding.spinnerArea, "Select Area")
                            disableSpinner(binding.spinnerArea)

                            areaViewModel.getArea(selectedSubdistrictId)
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
                currentAreaList = list
                val names = listOf("Select Area") + list.map { a -> a.areaName }
                binding.spinnerArea.adapter =
                    CustomSpinnerAdapter(requireContext(), names, setOf("Select Area"))

                enableSpinner(binding.spinnerArea)

                if (list.isNotEmpty()) {
                    binding.spinnerArea.setSelection(1)
                    selectedAreaId = list[0].areaNo
                }

                binding.spinnerArea.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int, id: Long
                        ) {
                            if (position > 0) {
                                selectedAreaId = list[position - 1].areaNo
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
            }
        }
    }

    private fun resetSpinner(spinner: Spinner, defaultText: String) {
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, listOf(defaultText)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun disableSpinner(spinner: Spinner) {
        spinner.isEnabled = false
        spinner.alpha = 0.5f
    }

    private fun enableSpinner(spinner: Spinner) {
        spinner.isEnabled = true
        spinner.alpha = 1f
    }

    //PERMISSIONS
    private fun getPermissionRequest(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                // All permissions granted
                getCurrentLocation()
                //Toast.makeText(requireContext(), "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission(s) denied, check for "Don't ask again" case
                var permanentlyDenied = false
                permissionList.forEach { permission ->
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(), permission
                        ) != PackageManager.PERMISSION_GRANTED && !shouldShowRequestPermissionRationale(
                            permission
                        )
                    ) {
                        permanentlyDenied = true
                    }
                }

                if (permanentlyDenied) {
                    // Show dialog to open app settings
                    showOpenSettingsDialog()
                } else {
                    Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showOpenSettingsDialog() {
        AlertDialog.Builder(requireContext()).setTitle("Permissions required")
            .setMessage("To create a store, you need to grant all permissions. Please open app settings to enable them.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }.show()
    }


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, exit or request again if needed
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLatitude = location.latitude
                currentLongitude = location.longitude

            } else {
                /*Toast.makeText(
                    requireContext(), "Could not get current location", Toast.LENGTH_SHORT
                ).show()*/
            }
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                when (currentImageType) {
                    ImageType.STORE_OWNER -> {
                        storeOwnerImageUri = fileUri
                        hasStoreOwnerUri = true
                        toggleSpinners()
                        binding.ivStoreOwner.setImageURI(fileUri)
                    }

                    ImageType.STORE -> {
                        storeImageUri = fileUri
                        hasStoreUri = true
                        toggleSpinners()
                        binding.ivUserStore.setImageURI(fileUri)
                    }

                    null -> {

                    }
                }
            } else if (resultCode == ImagePicker.Companion.RESULT_ERROR) {
                Toast.makeText(
                    requireContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream")
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
    }

    private fun launchImagePicker() {
        ImagePicker.with(this).cropSquare().cameraOnly().compress(1024).maxResultSize(512, 512)
            .createIntent { intent -> startForProfileImageResult.launch(intent) }
    }


    companion object {
        private val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }
}

