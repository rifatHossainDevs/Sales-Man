package com.wevx.dealershipmanagement.presentation.auth.profile

import android.widget.Toast
import androidx.fragment.app.viewModels
import coil.load
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentProfileBinding
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun setAllClickListener() {
        viewModel.getProfile()
        binding.btnEditProfile.setOnClickListener {
        }
    }

    override fun allObserver() {
        viewModel.profileState.collectInLifecycle(viewLifecycleOwner) { profileState ->
            if (profileState.loading) return@collectInLifecycle

            profileState.error?.let { error->
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
            }

            profileState.data?.let {data->
                Toast.makeText(requireContext(), "Success : $data", Toast.LENGTH_SHORT).show()
                binding.apply {
                    tvName.text = data.name
                    tvEmail.text = data.email
                    tvPhone.text = data.phone
                    tvUserType.text = data.userType
                    tvNid.text = data.nid
                    ivProfileImage.load(data.avatar)
                }
            }
        }
    }

}