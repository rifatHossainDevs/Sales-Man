package com.wevx.dealershipmanagement.presentation.auth.login

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.presentation.MainActivity
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.databinding.FragmentLoginBinding
import com.wevx.dealershipmanagement.databinding.PhoneVerificationBottomSheetBinding
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import com.wevx.dealershipmanagement.utils.extract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private lateinit var bottomSheetBinding: PhoneVerificationBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val loginViewModel: LoginViewModel by viewModels()

    private val permission = "true"

    override fun setAllClickListener() {
        bottomSheetBinding = PhoneVerificationBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetClickListener()

        allButtonClickListener()


    }


    override fun allObserver() {
        loginViewModel.loginState.collectInLifecycle(viewLifecycleOwner) { loginState ->
            if (loginState.loading) return@collectInLifecycle
            loginState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            loginState.data?.let {
                Toast.makeText(requireContext(), "Success : $it", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.show()
            }
        }
    }

    private fun bottomSheetClickListener() {
        bottomSheetDialog.apply {
            setContentView(bottomSheetBinding.root)
            setCancelable(true)
        }

        bottomSheetBinding.btnContinue.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

        bottomSheetBinding.btnClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

    }

    private fun allButtonClickListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                val phone = etPhoneNumber.extract()
                val password = etPassword.extract()
                val confirmPassword = etConfirmPassword.extract()
                if (checkAllFieldValidity(phone, password, confirmPassword)) {
                    val data = RequestLogin(
                        phone = phone,
                        password = password
                    )
                    loginViewModel.loginUser(data)
                }
            }
        }

        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.forgetPasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

    }

    private fun checkAllFieldValidity(
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        binding.etPhoneNumberLayout.error = null
        binding.etPasswordLayout.error = null
        binding.etConfirmPasswordLayout.error = null

        if (phone == "") {
            binding.etPhoneNumberLayout.error = "This field must be filled"
            return false
        }
        if (phone.length < 11) {
            binding.etPhoneNumberLayout.error = "Phone number Should have at least 11 Digit"
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

        if (confirmPassword == "") {
            binding.etConfirmPasswordLayout.error = "This field must be filled"
            return false
        }

        if (password != confirmPassword) {
            binding.etConfirmPasswordLayout.error = "Password and Confirm Password are not match!"
            return false
        }

        return true
    }
}