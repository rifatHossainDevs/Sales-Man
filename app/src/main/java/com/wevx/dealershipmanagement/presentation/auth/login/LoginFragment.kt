package com.wevx.dealershipmanagement.presentation.auth.login

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.presentation.MainActivity
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.RequestLogin
import com.wevx.dealershipmanagement.databinding.FragmentLoginBinding
import com.wevx.dealershipmanagement.databinding.PhoneVerificationBottomSheetBinding
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



        val data = RequestLogin(
            email = "uthoaimarma597@gmail.com",
            password = "P@5101054"
        )
        loginViewModel.loginUser(data)




    }

    override fun allObserver() {

    }

    private fun bottomSheetClickListener() {
        bottomSheetDialog.apply {
            setContentView(bottomSheetBinding.root)
            setCancelable(true)
        }

        binding.btnLogin.setOnClickListener {
            bottomSheetDialog.show()
        }
        bottomSheetBinding.btnClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnContinue.setOnClickListener {
            if (permission == "true"){
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }

    private fun allButtonClickListener() {
        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.forgetPasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

    }
}