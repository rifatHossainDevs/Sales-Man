package com.wevx.dealershipmanagement.presentation.paymentCollection

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentPaymentCollectionBinding
import com.wevx.dealershipmanagement.presentation.adapter.PendingOrderOfSellerAdapter
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileViewModel
import com.wevx.dealershipmanagement.utils.TokenManager
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentCollectionFragment : BaseFragment<FragmentPaymentCollectionBinding>(
    FragmentPaymentCollectionBinding::inflate
), PendingOrderOfSellerAdapter.SellerPendingOrderHandleClickListener {

    private val pendingPaymentViewModel: SellerPendingPaymentViewModel by viewModels()
    private val profileViewModel: GetProfileViewModel by viewModels()
    private lateinit var pendingPaymentAdapter: PendingOrderOfSellerAdapter
    private var sellerId: String = ""

    override fun setAllClickListener() {
        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getAccessToken().orEmpty()
        profileViewModel.getProfile("Bearer $token")
    }

    override fun onResume() {
        super.onResume()
        if (sellerId.isNotBlank()) {
            pendingPaymentViewModel.loadPendingPayments(sellerId)
        }
    }

    override fun allObserver() {
        profileObserver()
        pendingPaymentObserver()
    }

    private fun pendingPaymentObserver() {
        pendingPaymentViewModel.pendingPaymentState.collectInLifecycle(viewLifecycleOwner) { state ->
            if (state.loading) {
                loading.show()
                return@collectInLifecycle
            }

            state.error?.let {
                loading.dismiss()
                binding.rvPaymentCollection.visibility = View.INVISIBLE
                binding.tvNoPendingPayment.visibility = View.VISIBLE
            }

            state.data?.let { orders ->
                loading.dismiss()
                if (orders.isNotEmpty()) {
                    binding.rvPaymentCollection.visibility = View.VISIBLE
                    binding.tvNoPendingPayment.visibility = View.GONE
                    pendingPaymentAdapter = PendingOrderOfSellerAdapter(orders, this)
                    binding.rvPaymentCollection.adapter = pendingPaymentAdapter
                } else {
                    binding.rvPaymentCollection.visibility = View.INVISIBLE
                    binding.tvNoPendingPayment.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun profileObserver() {
        profileViewModel.profileState.collectInLifecycle(viewLifecycleOwner) { profileState ->
            if (profileState.loading) return@collectInLifecycle

            profileState.error?.let {
                binding.rvPaymentCollection.visibility = View.INVISIBLE
                binding.tvNoPendingPayment.visibility = View.VISIBLE
            }

            profileState.data?.let {
                sellerId = it.id
                pendingPaymentViewModel.loadPendingPayments(sellerId)
            }
        }
    }

    override fun selectPendingOrder(pendingOderId: String, customerId: String) {
        val action =
            PaymentCollectionFragmentDirections.actionPaymentCollectionFragmentToOrderDetailsFragment(
                pendingOderId,
                customerId
            )
        findNavController().navigate(action)
    }
}
