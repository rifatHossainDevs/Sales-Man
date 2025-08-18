package com.wevx.dealershipmanagement.presentation.storeOwnerDetails

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentStoreOwnerDetailsBinding
import com.wevx.dealershipmanagement.presentation.adapter.CompleteOrderAdapter
import com.wevx.dealershipmanagement.presentation.adapter.PendingOrderAdapter
import com.wevx.dealershipmanagement.presentation.order.completeOrder.CompleteOrderViewModel
import com.wevx.dealershipmanagement.presentation.order.pendingOrder.PendingOrderViewModel
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreOwnerDetailsFragment : BaseFragment<FragmentStoreOwnerDetailsBinding>(
    FragmentStoreOwnerDetailsBinding::inflate
), PendingOrderAdapter.PendingHandleClickListener,
    CompleteOrderAdapter.CompleteHandleClickListener {
    private val pendingOrderViewModel: PendingOrderViewModel by viewModels()
    private val completeOrderViewModel: CompleteOrderViewModel by viewModels()
    private lateinit var pendingOrderAdapter: PendingOrderAdapter
    private lateinit var completeOrderAdapter: CompleteOrderAdapter
    private val storeOwnerByIdViewModel: GetStoreByIdViewModel by viewModels()
    lateinit var customerId: String
    private val args: StoreOwnerDetailsFragmentArgs by navArgs()

    override fun setAllClickListener() {
        val userId = args.id
        customerId = args.id
        pendingOrderViewModel.getPendingAndCompleteOder(userId, "Pending")
        completeOrderViewModel.getPendingAndCompleteOder(userId, "Paid")
        Log.d("complete", "setAllClickListener: $userId")
        allButtonClickListener()
    }

    private fun allButtonClickListener() {
        val userId = args.id
        binding.btnCreateOrder.setOnClickListener {
            val action =
                StoreOwnerDetailsFragmentDirections.actionStoreOwnerDetailsFragmentToProductsFragment(
                    userId
                )
            findNavController().navigate(action)
        }
        storeOwnerByIdViewModel.getStoreById(userId)
    }

    override fun allObserver() {
        pendingOderObserver()
        completeOderObserver()
        storeOwnerByIdObserver()
    }

    private fun completeOderObserver() {
        completeOrderViewModel.completeOrderState.collectInLifecycle(viewLifecycleOwner) { completeOrderState ->

            if (completeOrderState.loading) {
                loading.show()
                return@collectInLifecycle
            }

            completeOrderState.error?.let {
                loading.dismiss()
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                //Toast.makeText(requireContext(), "Error: hello", Toast.LENGTH_SHORT).show()

            }

            completeOrderState.data?.let { completeOrderList ->
                loading.dismiss()
                completeOrderAdapter = CompleteOrderAdapter(completeOrderList, this)
                binding.rvCompleteOrder.adapter = completeOrderAdapter
            }
        }
    }

    private fun pendingOderObserver() {
        pendingOrderViewModel.pendingOrderState.collectInLifecycle(viewLifecycleOwner) { pendingOrderState ->
            if (pendingOrderState.loading) {
                loading.show()
                return@collectInLifecycle
            }

            pendingOrderState.error?.let {
                loading.dismiss()
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                Log.d("pending", "pendingOderObserver: $it")
            }
            pendingOrderState.data?.let { pendingOrderList ->
                loading.dismiss()
                pendingOrderAdapter = PendingOrderAdapter(pendingOrderList, this)
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

    override fun selectPendingOrder(pendingOderId: String) {

        val action =
            StoreOwnerDetailsFragmentDirections.actionStoreOwnerDetailsFragmentToOrderDetailsFragment(
                pendingOderId, customerId
            )
        findNavController().navigate(action)
    }

    override fun selectCompleteOrder(completeOrderid: String) {
        val action =
            StoreOwnerDetailsFragmentDirections.actionStoreOwnerDetailsFragmentToOrderDetailsFragment(
                completeOrderid, customerId
            )
        findNavController().navigate(action)

    }


}