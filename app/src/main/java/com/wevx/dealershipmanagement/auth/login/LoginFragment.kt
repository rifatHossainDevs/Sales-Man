package com.wevx.dealershipmanagement.auth.login

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.starter.MainActivity
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentLoginBinding
import com.wevx.dealershipmanagement.databinding.PhoneVerificationBottomSheetBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private lateinit var bottomSheetBinding: PhoneVerificationBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val permission = "true"

    override fun setAllClickListener() {
        bottomSheetBinding = PhoneVerificationBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetClickListener()

        allButtonClickListener()
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