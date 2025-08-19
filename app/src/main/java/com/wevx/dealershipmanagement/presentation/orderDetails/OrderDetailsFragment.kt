package com.wevx.dealershipmanagement.presentation.orderDetails

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.order.updateOrder.RequestUpdateOrder
import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.RequestUpdatePayment
import com.wevx.dealershipmanagement.data.dto.order.orderDetailsDTO.ResponseOderDetailsDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestUpdateShipment
import com.wevx.dealershipmanagement.databinding.FragmentOrderDetailsBinding
import com.wevx.dealershipmanagement.presentation.adapter.OrderDetailsAdapter
import com.wevx.dealershipmanagement.presentation.order.updateOrder.UpdateOrderViewModel
import com.wevx.dealershipmanagement.presentation.payment.getPayment.GetPaymentViewModel
import com.wevx.dealershipmanagement.presentation.payment.updatePayment.UpdatePaymentViewModel
import com.wevx.dealershipmanagement.presentation.shipment.getShipment.GetShipmentViewModel
import com.wevx.dealershipmanagement.presentation.shipment.updateShipment.UpdateShipmentViewModel
import com.wevx.dealershipmanagement.presentation.storeOwnerDetails.GetStoreByIdViewModel
import com.wevx.dealershipmanagement.utils.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private val getShipmentViewModel: GetShipmentViewModel by viewModels()
    private val updateShipmentViewModel: UpdateShipmentViewModel by viewModels()
    private var paymentID: String? = null
    private var paymentAmount: Double? = null
    private var shipmentID: String? = null
    private var expectedShipDate: String? = null
    private var shipmentStatus: String? = null
    private var paymentStatus: String? = null
    private var orderDetail: ResponseOderDetailsDTO? = null

    var customerId: String? = null
    var orderId: String? = null

    @RequiresApi(Build.VERSION_CODES.O)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerId = args.customerId
        orderId = args.orderId

        if (orderId != null && customerId != null){
            storeOwnerByIdViewModel.getStoreById(customerId!!)
            orderDetailsViewModel.getOderDetails(orderId!!)
            orderDetailsViewModel.getOderDetailsDEMO(orderId!!)
            getPaymentViewModel.getPaymentByOrderId(orderId!!)
            getShipmentViewModel.getShipmentByOrderId(orderId!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setAllClickListener() {

        binding.btnCompletePayment.setOnClickListener {
            handlePaymentComplete()
        }

        binding.btnCompleteShipment.setOnClickListener {
            handleShipmentComplete()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleShipmentComplete() {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        if (shipmentID != null && expectedShipDate != null){
            updateShipmentViewModel.updateShipment(
                id = shipmentID!!,
                requestUpdateShipment = RequestUpdateShipment(
                    shipmentStatus = "Delivered",
                    expectedShipDate = expectedShipDate,
                    shippedAt = currentDateTime
                )
            )
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
        getShipmentObserver()
        updateShipmentObserver()
        orderDetailsDemoObserver()
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

    private fun orderDetailsDemoObserver() {
        lifecycleScope.launch {
            orderDetailsViewModel.orderDetailsStateDemo.collect { response ->
                if (response.loading){
                    //loading.show()
                }
                if (response.error != null){
                    loading.dismiss()
                }
                if (response.data != null){
                    loading.dismiss()
                    orderDetail = response.data
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

                    paymentStatus = response.data.data?.get(0)?.paymentStatus
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
                    paymentStatus = response.data.data?.paymentStatus

                    // btn handle
                    if (
                        response.data.data?.paymentStatus == "Paid"
                    ){
                        binding.btnCompletePayment.apply {
                            isEnabled = false
                            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                        }
                        if (shipmentStatus == "Delivered"){
                            handleUpdateOrder()
                        }
                    }
                }
            }
        }
    }

    private fun updateShipmentObserver() {
        lifecycleScope.launch {
            updateShipmentViewModel.updateShipmentState.collect{ response ->
                if (response.loading){
                    loading.show()
                }
                if (response.error != null){
                    loading.dismiss()
                }
                if (response.data != null){
                    loading.dismiss()

                    shipmentStatus = response.data.data?.shipmentStatus

                    // btn handle
                    if (
                        response.data.data?.shipmentStatus == "Delivered"
                    ){
                        binding.btnCompleteShipment.apply {
                            isEnabled = false
                            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
                        }

                        if (paymentStatus == "Paid"){
                            handleUpdateOrder()
                        }
                    }
                }
            }
        }
    }

    private fun getShipmentObserver() {
        lifecycleScope.launch {
            getShipmentViewModel.getShipmentState.collect{ response ->
                if (response.loading){
                    //loading.show()
                }
                if (response.error != null){
                    loading.dismiss()
                }
                if (response.data != null){
                    loading.dismiss()
                    shipmentID = response.data.data?.get(0)?.id
                    expectedShipDate = response.data.data?.get(0)?.expectedShipDate

                    Log.d("TAG", "shipmentID: $shipmentID")
                    shipmentStatus = response.data.data?.get(0)?.shipmentStatus

                    // btn handle
                    if (
                        response.data.data?.get(0)?.shipmentStatus == "Delivered"
                    ){
                        binding.btnCompleteShipment.apply {
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
                }
            }
        }
    }


    private fun handleUpdateOrder(){
        val requestUpdateOrder = RequestUpdateOrder(
            paymentStatus= "Paid",
        )
        if (orderId != null){
            updateOrderViewModel.updateOrder(
                id = orderId.toString(),
                requestUpdateOrder = requestUpdateOrder,
            )
        }
    }


}




