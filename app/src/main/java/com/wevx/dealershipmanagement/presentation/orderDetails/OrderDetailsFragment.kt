package com.wevx.dealershipmanagement.presentation.orderDetails

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.RequestUpdatePayment
import com.wevx.dealershipmanagement.data.dto.ResponseGetPaymentDTO
import com.wevx.dealershipmanagement.databinding.FragmentOrderDetailsBinding
import com.wevx.dealershipmanagement.presentation.adapter.OrderDetailsAdapter
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.GetStoreByIdViewModel
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.StoreOwnerDetailsFragmentArgs
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.get
import kotlin.getValue

@AndroidEntryPoint
class OrderDetailsFragment :
    BaseFragment<FragmentOrderDetailsBinding>(FragmentOrderDetailsBinding::inflate) {

    val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    private val getPaymentViewModel: GetPaymentViewModel by viewModels()
    private val updatePaymentViewModel: UpdatePaymentViewModel by viewModels()

    private val args: OrderDetailsFragmentArgs by navArgs()
    lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private val storeOwnerByIdViewModel: GetStoreByIdViewModel by viewModels()
    private val updateOrderViewModel: UpdateOrderViewModel by viewModels()


    private var paymentID: String? = null
    private var paymentAmount: Double? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setAllClickListener() {
        val customerId = args.customerId
        val orderId = args.orderId
        storeOwnerByIdViewModel.getStoreById(customerId)
        orderDetailsViewModel.getOderDetails(orderId)
        getPaymentViewModel.getPaymentByOrderId(orderId)


        binding.btnCompletePayment.setOnClickListener {
            handlePaymentComplete()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlePaymentComplete() {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

        if (paymentID != null && paymentAmount != null){
            val requestUpdatePayment = RequestUpdatePayment(
                amount = paymentAmount,
                paidAt = currentDateTime,
                paymentStatus = "Paid",
                transactionId = ""
            )
            updatePaymentViewModel.updatePayment(
                paymentID!!,
                requestUpdatePayment = requestUpdatePayment
            )
        }
    }

    override fun allObserver() {
        orderDetailsObserver()
        storeOwnerObserver()
        getPaymentObserver()
        updatePaymentObserver()
        updateOrderObserver()
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


    private fun getPaymentObserver() {
        lifecycleScope.launch {
            getPaymentViewModel.getPaymentState.collect{ response ->
                if (response.loading){
                    //loading.show()
                }
                if (response.error != null){
                    loading.dismiss()
                }
                if (response.data != null){
                    loading.dismiss()
                    paymentID = response.data.data?.get(0)?.id
                    paymentAmount = response.data.data?.get(0)?.amount?.toDouble()

                    // btn handle
                    if (
                        response.data.data?.get(0)?.paymentStatus == "Paid"
                    ){
                        binding.btnCompletePayment.apply {
                            isEnabled = false
                            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                        }
                    }
                }
            }
        }
    }


    private fun updatePaymentObserver() {
        lifecycleScope.launch {
            updatePaymentViewModel.updatePaymentState.collect{ response ->
                if (response.loading){
                    loading.show()
                }
                if (response.error != null){
                    loading.dismiss()
                    Log.d("TAG", "update Payment error: ${response.error}")
                }
                if (response.data != null){
                    loading.dismiss()
                    Log.d("TAG", "getPaymentObserver: ${response.data}")
                    Log.d("TAG", "update Payment success")

                    // btn handle
                    if (
                        response.data.data?.paymentStatus == "Paid"
                    ){
                        binding.btnCompletePayment.apply {
                            isEnabled = false
                            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                        }
                    }
                }
            }
        }
    }


    private fun updateOrderObserver() {
        lifecycleScope.launch {
            updateOrderViewModel.updateOrderState.collect{ response ->
                if (response.loading){
                    loading.show()
                }
                if (response.error != null){
                    loading.dismiss()
                    Log.d("TAG", "update order error: ${response.error}")
                }
                if (response.data != null){
                    loading.dismiss()
                    Log.d("TAG", "update order success: ${response.data}")

                    /*// btn handle
                    if (
                        response.data.data?.paymentStatus == "Paid"
                    ){
                        binding.btnCompletePayment.apply {
                            isEnabled = false
                            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                        }
                    }*/
                }
            }
        }
    }


}




