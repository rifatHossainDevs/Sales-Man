package com.wevx.dealershipmanagement.presentation.storeOwnerDetails

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentStoreOwnerDetailsBinding
import com.wevx.dealershipmanagement.presentation.adapter.PendingOrderAdapter
import com.wevx.dealershipmanagement.presentation.order.pendingOrder.PendingOrderViewModel
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreOwnerDetailsFragment : BaseFragment<FragmentStoreOwnerDetailsBinding>(
    FragmentStoreOwnerDetailsBinding::inflate
) {
    private val pendingOrderViewModel: PendingOrderViewModel by viewModels()
    private lateinit var pendingOrderAdapter: PendingOrderAdapter
    private lateinit var completeOrderAdapter: PendingOrderAdapter

    override fun setAllClickListener() {
        allButtonClickListener()
    }

    private fun allButtonClickListener() {
        binding.btnCreateOrder.setOnClickListener {
            findNavController().navigate(R.id.action_storeOwnerDetailsFragment_to_productsFragment)
        }

        pendingOrderViewModel.getAllProduct("6892625886d113d59994c48a")
    }

    override fun allObserver() {
        pendingOrderViewModel.pendingOrderState.collectInLifecycle(viewLifecycleOwner) { pendingOrderState ->
            if (pendingOrderState.loading) return@collectInLifecycle
            pendingOrderState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
            pendingOrderState.data?.let {pendingOrderList->
                pendingOrderAdapter = PendingOrderAdapter(pendingOrderList, "pending")
                completeOrderAdapter = PendingOrderAdapter(pendingOrderList, "complete")
                binding.rvPendingOrder.adapter = pendingOrderAdapter
                binding.rvCompleteOrder.adapter = pendingOrderAdapter

            }


        }
    }

}