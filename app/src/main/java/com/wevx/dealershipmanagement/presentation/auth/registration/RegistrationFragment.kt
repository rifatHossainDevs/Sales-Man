package com.wevx.dealershipmanagement.presentation.auth.registration

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.utils.areAllPermissionGranted
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistrationDto
import com.wevx.dealershipmanagement.databinding.FragmentRegistrationBinding
import com.wevx.dealershipmanagement.databinding.PhoneVerificationBottomSheetBinding
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import com.wevx.dealershipmanagement.utils.extract
import com.wevx.dealershipmanagement.utils.requestPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {
    private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var bottomSheetBinding: PhoneVerificationBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun setAllClickListener() {

        bottomSheetClickListener()

        allButtonClickListener()
        uploadButtonClickListener()
        permissionRequest = getPermissionRequest()

    }

    private fun bottomSheetClickListener() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetBinding = PhoneVerificationBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.apply {
            setContentView(bottomSheetBinding.root)
            setCancelable(true)
        }

        bottomSheetBinding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    private fun uploadButtonClickListener() {
        binding.btnUploadImage.setOnClickListener {
            requestPermission(permissionRequest, permissionList)
        }
    }

    override fun allObserver() {
        registrationViewModel.registrationState.collectInLifecycle(viewLifecycleOwner) { registrationState ->
            if (registrationState.loading) return@collectInLifecycle
            registrationState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            registrationState.data?.let {
                Toast.makeText(requireContext(), "Success: $it", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.show()
            }
        }
    }

    private fun allButtonClickListener() {
        binding.apply {
            btnCreateAccount.setOnClickListener {
                val name = etUserName.extract()
                val phone = etPhoneNumber.extract()
                val email = etEmail.extract()
                val nid = etNid.extract()
                val password = etPassword.extract()
                val confirmPassword = etConfirmPassword.extract()


                if (checkAllFieldValidity(
                        name,
                        phone,
                        email,
                        nid,
                        password,
                        confirmPassword
                    )
                ) {
                    val requestRegistration = RequestRegistrationDto(
                        email = email,
                        fullName = name,
                        phone = phone,
                        password = password,
                        nidNumber = nid,
                        userType = "seller"
                    )
                    registrationViewModel.registrationUser(requestRegistration)

                }
            }

            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            }
        }

    }

    private fun getPermissionRequest(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (areAllPermissionGranted(permissionList)) {
                ImagePicker.with(this).cropSquare().compress(1024).maxResultSize(
                    512, 512
                ).createIntent { intent ->
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
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        )
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                if (fileUri.toString() != "") {
                    binding.ivUser.setImageURI(fileUri)
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkAllFieldValidity(
        name: String,
        phone: String,
        email: String,
        nid: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val emailPattern = "^[a-z0-9+_.-]+@[a-z.-]{4,7}\\.[a-z]{2,5}$"
        val passwordPattern =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>~_-]).{8,}\$"

        if (name == "") {
            binding.etUserNameLayout.error = "This field must be filled"
            return false
        }

        if (phone == "") {
            binding.etPhoneNumberLayout.error = "This field must be filled"
            return false
        }
        if (phone.length < 11) {
            binding.etPhoneNumberLayout.error = "Password Should have at least 11 Digit"
            return false
        }

        if (email == "") {
            binding.etEmailLayout.error = "This field must be filled"
            return false
        }
        if (!email.matches(emailPattern.toRegex())) {
            binding.etEmailLayout.error = "Invalid Email Format"
            return false
        }
        if (password == "") {
            binding.etPasswordLayout.error = "This field must be filled"
            return false
        }

        if (password.length < 8) {
            binding.etPasswordLayout.error = "Password Should have at least 8 Characters"
            return false
        }
        if (!password.matches(passwordPattern.toRegex())) {
            binding.etPasswordLayout.error =
                "At least one capital letter, small letter, digit and symbol"
            return false
        }

        if (password != confirmPassword) {
            binding.etConfirmPasswordLayout.error = "Password and Confirm Password are not match!"
            return false
        }

        if (nid == "") {
            binding.etConfirmPasswordLayout.error = "This field must be filled"
            return false
        }

        if (nid.length != 10 && nid.length != 17) {
            binding.etNidLayout.error = "NID Should have 10 or 17 Digit"
            return false
        }


        return true
    }
}