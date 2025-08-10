package com.wevx.dealershipmanagement.presentation.storeOwnerDetails

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
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
    private lateinit var userId: String

    private val storeOwnerByIdViewModel: GetStoreByIdViewModel by viewModels()

    private val args: StoreOwnerDetailsFragmentArgs by navArgs()

    override fun setAllClickListener() {
        userId = args.id
        allButtonClickListener()
    }

    private fun allButtonClickListener() {
        binding.btnCreateOrder.setOnClickListener {
            findNavController().navigate(R.id.action_storeOwnerDetailsFragment_to_productsFragment)
        }
        storeOwnerByIdViewModel.getStoreById(userId)
        pendingOrderViewModel.getAllProduct("6892625886d113d59994c48a")
    }

    override fun allObserver() {
        pendingOderObserver()
        storeOwnerByIdObserver()
    }

    private fun storeOwnerByIdObserver() {
        storeOwnerByIdViewModel.getStoreByIdState.collectInLifecycle(viewLifecycleOwner) { storeOwnerByIdState ->
            if (storeOwnerByIdState.loading) return@collectInLifecycle
            storeOwnerByIdState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            storeOwnerByIdState.data?.let {
                binding.tvStoreName.text = it.storeName
                binding.tvOwnerName.text = it.storeOwnerName
                binding.tvAddress.text = it.address
                binding.ivStoreOwner.load(it.storeOwnerAvatar)

            }
        }

    }

    private fun pendingOderObserver() {
        pendingOrderViewModel.pendingOrderState.collectInLifecycle(viewLifecycleOwner) { pendingOrderState ->
            if (pendingOrderState.loading) return@collectInLifecycle
            pendingOrderState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
            pendingOrderState.data?.let { pendingOrderList ->
                pendingOrderAdapter = PendingOrderAdapter(pendingOrderList, "pending")
                completeOrderAdapter = PendingOrderAdapter(pendingOrderList, "complete")
                binding.rvPendingOrder.adapter = pendingOrderAdapter
                binding.rvCompleteOrder.adapter = pendingOrderAdapter

            }


        }
    }

}