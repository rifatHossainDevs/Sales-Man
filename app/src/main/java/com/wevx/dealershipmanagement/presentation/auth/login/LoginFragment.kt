package com.wevx.dealershipmanagement.presentation.auth.login

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.presentation.MainActivity
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.RequestRefreshToken
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
    private val refreshTokenViewModel: RefreshTokenViewModel by viewModels()

    override fun shouldInitialize(): Boolean {
        refreshTokenObserver()
        val tokenManager = TokenManager(requireContext())
        val refreshToken = tokenManager.getRefreshToken().toString()
        refreshTokenViewModel.getRefreshAccessToken(RequestRefreshToken(refreshToken))
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
            if (refreshTokenState.loading) {
                loading.show()
            }

            refreshTokenState.error?.let {
                loading.dismiss()
            }

            refreshTokenState.data?.let {
                if (it.data?.accessToken?.isNotEmpty() == true) {
                    loading.dismiss()
                    val tokenManager = TokenManager(requireContext())
                    tokenManager.saveToken(
                        it.data.accessToken, "${it.data.refreshToken}"
                    )
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun loginObserver() {
        loginViewModel.loginState.collectInLifecycle(viewLifecycleOwner) { loginState ->
            if (loginState.loading) {
                loading.show()
                return@collectInLifecycle
            }

            loginState.error?.let {
                loading.dismiss()
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            loginState.data?.let { responseDTO ->
                loading.dismiss()
                val tokenManager = TokenManager(requireContext())

                tokenManager.saveToken(
                    "${responseDTO.accessToken}", "${responseDTO.refreshToken}"
                )

                Log.d("refresh", "loginObserver: ${responseDTO.accessToken}")


                //Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()


                if (responseDTO.isActive) {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Account Inactive")
                        .setMessage("Your account is not active. Please contact with your company.")
                        .setPositiveButton("Got it") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }

            }
        }
    }


    private fun allButtonClickListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                val userEmailOrPass = etEmailOrPhone.extract()
                val password = etPassword.extract()

                val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

                if (userEmailOrPass.isNotEmpty() && password.isNotEmpty()) {
                    if (emailPattern.matches(userEmailOrPass)) {
                        loginViewModel.loginUser(
                            RequestLogin(
                                email = userEmailOrPass,
                                password = password
                            )
                        )
                    } else {
                        loginViewModel.loginUser(
                            RequestLogin(
                                phone = userEmailOrPass,
                                password = password
                            )
                        )
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enter email and password",
                        Toast.LENGTH_SHORT
                    ).show()
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


}