package com.wevx.dealershipmanagement.presentation.auth.updateProfile

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.updateProfileDto.RequestUpdateProfile
import com.wevx.dealershipmanagement.databinding.FragmentEditProfileBinding
import com.wevx.dealershipmanagement.presentation.MainActivity
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import com.wevx.dealershipmanagement.utils.extract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {
    private lateinit var token: String
    val updateProfileViewModel: UpdateProfileViewModel by viewModels()
    val getProfileViewModel: GetProfileViewModel by viewModels()
    override fun setAllClickListener() {
        val tokenManager = TokenManager(requireContext())
        token = "Bearer ${tokenManager.getAccessToken()}"

        allButtonClickListener()

        getProfileViewModel.getProfile(token)
    }


    private fun allButtonClickListener() {
        binding.apply {
            btnUpdate.setOnClickListener {
                val name = etFullName.extract()
                val email = etEmail.extract()
                val phone = etPhoneNumber.extract()
                val nid = etNidNumber.extract()
                if (checkAllFieldValidity(name, email, phone, nid)) {
                    val requestUpdateProfile = RequestUpdateProfile(
                        fullName = name,
                        email = email,
                        phone = phone,
                        nidNumber = nid
                    )
                    updateProfileViewModel.updateProfile(requestUpdateProfile, token)
                }
            }
        }

    }

    override fun allObserver() {
        getProfileObserver()

        updateProfileObserver()

    }

    private fun getProfileObserver() {
        getProfileViewModel.profileState.collectInLifecycle(viewLifecycleOwner) { profileState ->
            if (profileState.loading) return@collectInLifecycle
            profileState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
            profileState.data?.let { profileModel ->
                binding.etFullName.setText(profileModel.name)
                binding.etEmail.setText(profileModel.email)
                binding.etPhoneNumber.setText(profileModel.phone)
                binding.etNidNumber.setText(profileModel.nid)
            }
        }
    }

    private fun updateProfileObserver() {
        updateProfileViewModel.updateProfileState.collectInLifecycle(viewLifecycleOwner) { updateProfileState ->
            if (updateProfileState.loading) return@collectInLifecycle
            updateProfileState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            updateProfileState.data?.let { updateProfileModel ->
                if (updateProfileModel.success) {
                    Toast.makeText(
                        requireContext(),
                        "Account details updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_editProfileFragment_to_homeFragment)
                }
            }
        }
    }


    private fun checkAllFieldValidity(
        name: String,
        email: String,
        phone: String,
        nid: String
    ): Boolean {

        binding.etFullNameLayout.error = null
        binding.etEmailLayout.error = null
        binding.etPhoneNumberLayout.error = null
        binding.etNidNumberLayout.error = null

        val emailPattern = "^[a-z0-9+_.-]+@[a-z.-]{4,7}\\.[a-z]{2,5}$"

        if (name == "") {
            binding.etFullNameLayout.error = "This field must be filled"
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

        if (nid == "") {
            binding.etNidNumberLayout.error = "This field must be filled"
            return false
        }

        /*if (nid.length != 10 && nid.length != 17) {
            binding.etNidNumberLayout.error = "NID Should have 10 or 17 Digit"
            return false
        }*/

        return true
    }

}