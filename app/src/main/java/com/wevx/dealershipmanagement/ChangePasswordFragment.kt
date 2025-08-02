package com.wevx.dealershipmanagement

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.RequestChangePasswordDto
import com.wevx.dealershipmanagement.databinding.FragmentChangePasswordBinding
import com.wevx.dealershipmanagement.presentation.AuthActivity
import com.wevx.dealershipmanagement.presentation.auth.changePassword.ChangePasswordViewModel
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import com.wevx.dealershipmanagement.utils.extract

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>(
    FragmentChangePasswordBinding::inflate
) {
    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun setAllClickListener() {

        binding.apply {
            btnChangePassword.setOnClickListener {
                val oldPassword = etOldPassword.extract()
                val newPassword = etNewPassword.extract()

                val requestChangePassword = RequestChangePasswordDto(
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )
                viewModel.changePassword(requestChangePassword)
            }
        }

    }


    override fun allObserver() {
        viewModel.changePasswordState.collectInLifecycle(viewLifecycleOwner) { changePasswordState ->
            if (changePasswordState.loading) return@collectInLifecycle

            changePasswordState.error?.let { error ->
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
            }

            changePasswordState.data?.let { data ->
                //Toast.makeText(requireContext(), "Success : $data", Toast.LENGTH_SHORT).show()
                if (data.success == true) {
                    Toast.makeText(
                        requireContext(),
                        "Password Changed Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }
}