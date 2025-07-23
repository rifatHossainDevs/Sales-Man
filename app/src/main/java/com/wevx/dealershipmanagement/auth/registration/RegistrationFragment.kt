package com.wevx.dealershipmanagement.auth.registration

import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.base.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentRegistrationBinding

class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {
    override fun setAllClickListener() {

        allButtonClickListener()
        uploadButtonClickListener()
    }

    private fun uploadButtonClickListener() {

    }

    override fun allObserver() {

    }

    private fun allButtonClickListener() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }
}