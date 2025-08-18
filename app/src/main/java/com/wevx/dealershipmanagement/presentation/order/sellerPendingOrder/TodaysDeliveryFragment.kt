package com.wevx.dealershipmanagement.presentation.order.sellerPendingOrder

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentTodaysDeliveryBinding
import com.wevx.dealershipmanagement.presentation.adapter.PendingOrderOfSellerAdapter
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.StoreOwnerDetailsFragmentDirections
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodaysDeliveryFragment : BaseFragment<FragmentTodaysDeliveryBinding>(
    FragmentTodaysDeliveryBinding::inflate
), PendingOrderOfSellerAdapter.SellerPendingOrderHandleClickListener {
    private val pendingOderViewModel: SellerPendingOrderViewModel by viewModels()

    private val profileViewModel: GetProfileViewModel by viewModels()
    lateinit var pendingOrderAdapter: PendingOrderOfSellerAdapter

    lateinit var sellerId: String

    override fun setAllClickListener() {
        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getAccessToken().toString()
        profileViewModel.getProfile(token)

    }

    override fun allObserver() {
        profileObserver()
        pendingOrderObserver()

    }

    private fun pendingOrderObserver() {
        pendingOderViewModel.sellerPendingState.collectInLifecycle(viewLifecycleOwner) { sellerPendingState ->
            if (sellerPendingState.loading) {
                loading.show()
                return@collectInLifecycle
            }

            sellerPendingState.error?.let {
                loading.dismiss()
                binding.tvTodaysDelivery.visibility = View.VISIBLE
            }

            sellerPendingState.data?.let { order ->
                loading.dismiss()
                if (order.isNotEmpty()) {

                    pendingOrderAdapter = PendingOrderOfSellerAdapter(order, this)
                    binding.rvTodaysDelivery.adapter = pendingOrderAdapter
                } else {
                    binding.apply {
                        rvTodaysDelivery.visibility = View.INVISIBLE
                        tvTodaysDelivery.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    private fun profileObserver() {
        profileViewModel.profileState.collectInLifecycle(viewLifecycleOwner) { profileState ->
            if (profileState.loading) return@collectInLifecycle

            profileState.error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            profileState.data?.let {
                sellerId = it.id
                pendingOderViewModel.sellerPendingOrder(sellerId, "Pending")
            }

        }
    }

    override fun selectPendingOrder(pendingOderId: String, customerId: String) {
        val action =
            StoreOwnerDetailsFragmentDirections.actionStoreOwnerDetailsFragmentToOrderDetailsFragment(
                pendingOderId, customerId
            )
        findNavController().navigate(action)
    }


}