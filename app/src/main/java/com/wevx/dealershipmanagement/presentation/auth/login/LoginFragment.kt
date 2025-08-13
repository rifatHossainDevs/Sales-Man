package com.wevx.dealershipmanagement.presentation.auth.login

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.presentation.MainActivity
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.databinding.FragmentLoginBinding
import com.wevx.dealershipmanagement.presentation.auth.refreshToken.RefreshTokenViewModel
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import com.wevx.dealershipmanagement.utils.extract
import dagger.hilt.android.AndroidEntryPoint
import com.wevx.dealershipmanagement.utils.TokenManager

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val loginViewModel: LoginViewModel by viewModels()
    private val refreshTokenViewModel: RefreshTokenViewModel by viewModels ()

    override fun shouldInitialize(): Boolean {
        val tokenManager = TokenManager(requireContext())
        if (tokenManager.hasValidTokens()) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
            return false
        }
        return true
    }

    override fun setAllClickListener() {

        allButtonClickListener()

    }

    override fun allObserver() {
        loginObserver()
        refreshTokenObserver()

    }

    private fun refreshTokenObserver() {
        refreshTokenViewModel.refreshTokenState.collectInLifecycle(viewLifecycleOwner) { refreshTokenState ->
            if (refreshTokenState.loading) return@collectInLifecycle

            refreshTokenState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            refreshTokenState.data?.let {

            }
        }
    }

    private fun loginObserver() {
        loginViewModel.loginState.collectInLifecycle(viewLifecycleOwner) { loginState ->
            if (loginState.loading) return@collectInLifecycle

            loginState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            loginState.data?.let { responseDTO ->
                val tokenManager = TokenManager(requireContext())

                tokenManager.saveToken(
                    "${responseDTO.accessToken}",
                    "${responseDTO.refreshToken}"
                )


                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()

                /*if (responseDTO.isActive){

                }else{
                    AlertDialog.Builder(requireContext())
                        .setTitle("Account Inactive")
                        .setMessage("Your account is not active. Please contact with your company.")
                        .setPositiveButton("Got it") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }*/

            }
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