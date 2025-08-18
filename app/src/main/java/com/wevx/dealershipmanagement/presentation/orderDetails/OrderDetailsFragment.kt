package com.wevx.dealershipmanagement.presentation.orderDetails

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.databinding.FragmentOrderDetailsBinding
import com.wevx.dealershipmanagement.presentation.adapter.OrderDetailsAdapter
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.GetStoreByIdViewModel
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.StoreOwnerDetailsFragmentArgs
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class OrderDetailsFragment :
    BaseFragment<FragmentOrderDetailsBinding>(FragmentOrderDetailsBinding::inflate) {

    val orderDetailsViewModel: OrderDetailsViewModel by viewModels()

    private val args: OrderDetailsFragmentArgs by navArgs()
    lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private val storeOwnerByIdViewModel: GetStoreByIdViewModel by viewModels()

    override fun setAllClickListener() {
        val customerId = args.customerId
        val orderId = args.orderId
        storeOwnerByIdViewModel.getStoreById(customerId)
        orderDetailsViewModel.getOderDetails(orderId)
    }

    override fun allObserver() {
        orderDetailsObserver()
        storeOwnerObserver()
    }

    private fun storeOwnerObserver() {
        storeOwnerByIdViewModel.getStoreByIdState.collectInLifecycle(viewLifecycleOwner){storeOwnerState->
            if (storeOwnerState.loading){
                loading.show()
            }
            storeOwnerState.error?.let {
                loading.dismiss()
            }

            storeOwnerState.data?.let { data->
                if (data != null){
                    binding.apply {
                        tvStoreName.text = data.storeName
                        tvOwnerName.text = data.storeOwnerName
                        tvAddress.text = data.address
                        Glide.with(requireContext())
                            .load(data.storeOwnerAvatar.replace("http://", "https://"))
                            .placeholder(R.drawable.ic_profile_image_24)
                            .error(R.drawable.ic_edit_profile)
                            .into(ivStoreOwner)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun orderDetailsObserver() {
        orderDetailsViewModel.orderDetailsState.collectInLifecycle(viewLifecycleOwner) { orderDetailsState ->
            if (orderDetailsState.loading){
                loading.show()
                return@collectInLifecycle
            }

            orderDetailsState.error?.let {
                loading.dismiss()
                //Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }

            orderDetailsState.data?.let { orderItem ->
                loading.dismiss()
                if (orderItem.isNotEmpty()) {
                    binding.tvTotal.text = "Total: ${orderItem.sumOf { it.subtotal }}"
                    orderDetailsAdapter = OrderDetailsAdapter(orderItem)
                    binding.recyclerProducts.adapter = orderDetailsAdapter
                }
            }
        }
    }


}