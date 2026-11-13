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
import java.time.Instant
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

        // Read arguments passed through Navigation Component
        orderId = args.orderId
        customerId = args.customerId

        Log.d("ORDER_DEBUG", "Order ID from navigation: $orderId")
        Log.d("ORDER_DEBUG", "Customer ID: $customerId")

        if (!orderId.isNullOrBlank() && !customerId.isNullOrBlank()) {

            storeOwnerByIdViewModel.getStoreById(customerId!!)

            orderDetailsViewModel.getOderDetails(orderId!!)
            orderDetailsViewModel.getOderDetailsDEMO(orderId!!)

            Log.d("ORDER_DEBUG", "Calling Payment API with: $orderId")
            getPaymentViewModel.getPaymentByOrderId(orderId!!)

            Log.d("ORDER_DEBUG", "Calling Shipment API with: $orderId")
            getShipmentViewModel.getShipmentByOrderId(orderId!!)

        } else {
            Log.e("ORDER_DEBUG", "Navigation arguments are missing!")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun setAllClickListener() {

        binding.btnCompletePayment.setOnClickListener {
            Log.d("ORDER_DEBUG", "Payment button listener triggered")
            handlePaymentComplete()
        }

        binding.btnCompleteShipment.setOnClickListener {
            Log.d("ORDER_DEBUG", "Shipment button listener triggered")
            handleShipmentComplete()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleShipmentComplete() {
        Log.d("ORDER_DEBUG", "Complete Shipment clicked")

        Log.d(
            "ORDER_DEBUG",
            "shipmentID: $shipmentID, expectedShipDate: $expectedShipDate"
        )

        if (shipmentID.isNullOrBlank()) {
            Log.e("ORDER_DEBUG", "Shipment ID is null or empty")
            return
        }

        if (expectedShipDate.isNullOrBlank()) {
            Log.e("ORDER_DEBUG", "Expected ship date is null or empty")
            return
        }

        val currentDateTime = Instant.now().toString()

        val request = RequestUpdateShipment(
            shipmentStatus = "Delivered",
            expectedShipDate = expectedShipDate,
            shippedAt = currentDateTime
        )

        Log.d("ORDER_DEBUG", "Sending shipment update: $request")

        updateShipmentViewModel.updateShipment(
            id = shipmentID!!,
            requestUpdateShipment = request
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlePaymentComplete() {
        Log.d("ORDER_DEBUG", "Complete Payment clicked")

        Log.d(
            "ORDER_DEBUG",
            "paymentID: $paymentID, paymentAmount: $paymentAmount"
        )

        if (paymentID.isNullOrBlank()) {
            Log.e("ORDER_DEBUG", "Payment ID is null or empty")
            return
        }

        if (paymentAmount == null) {
            Log.e("ORDER_DEBUG", "Payment amount is null")
            return
        }

        val currentDateTime = Instant.now().toString()

        val request = RequestUpdatePayment(
            amount = paymentAmount,
            paidAt = currentDateTime,
            paymentStatus = "Paid",
            transactionId = ""
        )

        Log.d("ORDER_DEBUG", "Sending payment update: $request")

        updatePaymentViewModel.updatePayment(
            paymentID!!,
            requestUpdatePayment = request
        )
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
                Log.d("orderId", "orderDetailsObserver: $orderItem")
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
        viewLifecycleOwner.lifecycleScope.launch {
            getPaymentViewModel.getPaymentState.collect { response ->

                Log.d("ORDER_DEBUG", "Payment loading: ${response.loading}")
                Log.e("ORDER_DEBUG", "Payment error: ${response.error}")
                Log.d("ORDER_DEBUG", "Payment response: ${response.data}")

                if (response.data != null) {
                    val paymentList = response.data.data

                    Log.d("ORDER_DEBUG", "Payment list: $paymentList")
                    Log.d("ORDER_DEBUG", "Payment count: ${paymentList?.size}")

                    paymentID = paymentList?.firstOrNull()?.id
                    paymentAmount = paymentList?.firstOrNull()?.amount?.toDouble()

                    Log.d("ORDER_DEBUG", "Final paymentID: $paymentID")
                    Log.d("ORDER_DEBUG", "Final paymentAmount: $paymentAmount")
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
        viewLifecycleOwner.lifecycleScope.launch {
            getShipmentViewModel.getShipmentState.collect { response ->

                Log.d("ORDER_DEBUG", "Shipment loading: ${response.loading}")
                Log.e("ORDER_DEBUG", "Shipment error: ${response.error}")
                Log.d("ORDER_DEBUG", "Shipment response: ${response.data}")

                if (response.data != null) {
                    val shipmentList = response.data.data

                    Log.d("ORDER_DEBUG", "Shipment list: $shipmentList")
                    Log.d("ORDER_DEBUG", "Shipment count: ${shipmentList?.size}")

                    shipmentID = shipmentList?.firstOrNull()?.id
                    expectedShipDate = shipmentList?.firstOrNull()?.expectedShipDate

                    Log.d("ORDER_DEBUG", "Final shipmentID: $shipmentID")
                    Log.d("ORDER_DEBUG", "Final expectedShipDate: $expectedShipDate")
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




