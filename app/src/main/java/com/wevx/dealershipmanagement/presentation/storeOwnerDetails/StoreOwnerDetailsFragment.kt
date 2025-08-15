package com.wevx.dealershipmanagement.presentation.storeOwnerDetails

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.RequestPendingAndCompleteOrder
import com.wevx.dealershipmanagement.databinding.FragmentStoreOwnerDetailsBinding
import com.wevx.dealershipmanagement.presentation.adapter.PendingOrderAdapter
import com.wevx.dealershipmanagement.presentation.order.pendingAndCompleteOrder.PendingAndCompleteOrderViewModel
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreOwnerDetailsFragment : BaseFragment<FragmentStoreOwnerDetailsBinding>(
    FragmentStoreOwnerDetailsBinding::inflate
) {
    private val pendingOrderViewModel: PendingAndCompleteOrderViewModel by viewModels()

    private val completeOrderViewModel: PendingAndCompleteOrderViewModel by viewModels()
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
            val action =
                StoreOwnerDetailsFragmentDirections.actionStoreOwnerDetailsFragmentToProductsFragment(
                    userId
                )
            findNavController().navigate(action)
        }
        storeOwnerByIdViewModel.getStoreById(userId)

        val pendingOderRequest = RequestPendingAndCompleteOrder("Pending")
        val completeOderRequest = RequestPendingAndCompleteOrder("Paid")

        pendingOrderViewModel.getPendingAndCompleteOder(userId, pendingOderRequest)
        completeOrderViewModel.getPendingAndCompleteOder(userId, completeOderRequest)
    }

    override fun allObserver() {
        pendingOderObserver()
        completeOderObserver()
        storeOwnerByIdObserver()
    }

    private fun completeOderObserver() {
        completeOrderViewModel.pendingOrderState.collectInLifecycle(viewLifecycleOwner) { completeOrderState ->

            if (completeOrderState.loading) return@collectInLifecycle

            completeOrderState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()

            }

            completeOrderState.data?.let {
                //type use for color change
                completeOrderAdapter = PendingOrderAdapter(it, "complete")
                binding.rvCompleteOrder.adapter = completeOrderAdapter
            }
        }
    }

    private fun pendingOderObserver() {
        pendingOrderViewModel.pendingOrderState.collectInLifecycle(viewLifecycleOwner) { pendingOrderState ->
            if (pendingOrderState.loading) return@collectInLifecycle

            pendingOrderState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                Log.d("pending", "pendingOderObserver: $it")
            }
            pendingOrderState.data?.let { pendingOrderList ->
                //type use for color change
                pendingOrderAdapter = PendingOrderAdapter(pendingOrderList, "pending")
                binding.rvPendingOrder.adapter = pendingOrderAdapter

            }


        }
    }

    private fun storeOwnerByIdObserver() {
        storeOwnerByIdViewModel.getStoreByIdState.collectInLifecycle(viewLifecycleOwner) { storeOwnerByIdState ->
            if (storeOwnerByIdState.loading) return@collectInLifecycle
            storeOwnerByIdState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            storeOwnerByIdState.data?.let {
                binding.apply {
                    tvStoreName.text = it.storeName
                    tvOwnerName.text = it.storeOwnerName
                    tvAddress.text = it.address
                    Glide.with(requireContext())
                        .load(it.storeOwnerAvatar.replace("http://", "https://"))
                        .placeholder(R.drawable.ic_profile_image_24)
                        .error(R.drawable.ic_edit_profile)
                        .into(ivStoreOwner)
                }


            }
        }

    }

}