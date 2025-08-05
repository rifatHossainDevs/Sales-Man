package com.wevx.dealershipmanagement

import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentStoreOwnerDetailsBinding


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