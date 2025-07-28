package com.wevx.dealershipmanagement.presentation.auth.forgetPassword

import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentForgetPasswordBinding
import com.wevx.dealershipmanagement.databinding.PhoneVerificationBottomSheetBinding

class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>(
    FragmentForgetPasswordBinding::inflate
) {
    private lateinit var bottomSheetBinding: PhoneVerificationBottomSheetBinding
    override fun setAllClickListener() {
        bottomSheetBinding = PhoneVerificationBottomSheetBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.apply {
            setContentView(bottomSheetBinding.root)
            setCancelable(true)
        }

        binding.btnSendOtp.setOnClickListener {
            bottomSheetDialog.show()
        }

        bottomSheetBinding.btnClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
            bottomSheetDialog.dismiss()
        }
    }

    override fun allObserver() {

    }

}