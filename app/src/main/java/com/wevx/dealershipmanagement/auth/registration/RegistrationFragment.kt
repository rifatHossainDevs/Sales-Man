package com.wevx.dealershipmanagement.auth.registration

import android.Manifest
import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.areAllPermissionGranted
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentRegistrationBinding
import com.wevx.dealershipmanagement.requestPermission

class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>

    override fun setAllClickListener() {

        allButtonClickListener()
        uploadButtonClickListener()
        permissionRequest = getPermissionRequest()

        binding.btnUploadImage.setOnClickListener {
            requestPermission(permissionRequest, permissionList)
        }
    }

    private fun getPermissionRequest(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (areAllPermissionGranted(permissionList)) {
                ImagePicker.with(this)
                    .cropSquare()
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

    private fun uploadButtonClickListener() {

    }

    override fun allObserver() {

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
                    binding.ivUser.visibility = View.VISIBLE
                    binding.ivUser.setImageURI(fileUri)
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun allButtonClickListener() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }
}