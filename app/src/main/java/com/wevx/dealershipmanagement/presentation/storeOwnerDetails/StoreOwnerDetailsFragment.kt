package com.wevx.dealershipmanagement.presentation.storeOwnerDetails

import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentStoreOwnerDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreOwnerDetailsFragment : BaseFragment<FragmentStoreOwnerDetailsBinding>(
    FragmentStoreOwnerDetailsBinding::inflate
) {
    override fun setAllClickListener() {
        allButtonClickListener()
    }

    private fun allButtonClickListener() {
        binding.btnCreateOrder.setOnClickListener {
            findNavController().navigate(R.id.action_storeOwnerDetailsFragment_to_productsFragment)
        }
    }

    override fun allObserver() {

    }

}