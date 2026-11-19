package com.wevx.dealershipmanagement.presentation.orderDetails

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.core.common.BaseFragment
import com.wevx.dealershipmanagement.data.dto.order.orderDetailsDTO.ResponseOderDetailsDTO
import com.wevx.dealershipmanagement.data.dto.order.updateOrder.RequestUpdateOrder
import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.RequestUpdatePayment
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
import java.time.Instant
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

        orderId = args.orderId
        customerId = args.customerId

        Log.d("ORDER_DEBUG", "Order ID from navigation: $orderId")
        Log.d("ORDER_DEBUG", "Customer ID: $customerId")

        if (!orderId.isNullOrBlank() && !customerId.isNullOrBlank()) {
            storeOwnerByIdViewModel.getStoreById(customerId!!)
            orderDetailsViewModel.getOderDetails(orderId!!)
            orderDetailsViewModel.getOderDetailsDEMO(orderId!!)
            getPaymentViewModel.getPaymentByOrderId(orderId!!)
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

    private fun markPaymentButtonCompleted() {
        binding.btnCompletePayment.apply {
            isEnabled = false
            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
    }

    private fun markShipmentButtonCompleted() {
        binding.btnCompleteShipment.apply {
            isEnabled = false
            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleShipmentComplete() {
        Log.d("ORDER_DEBUG", "Complete Shipment clicked")

        // Enforce business rule matching Backend & Admin Panel: Payment must be Paid before Delivery
        if (paymentStatus != "Paid") {
            Toast.makeText(
                requireContext(),
                "Please complete payment first before marking shipment as delivered.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val currentDateTime = Instant.now().toString()
        val shipDate = expectedShipDate ?: orderDetail?.data?.expectedShipDate ?: currentDateTime

        if (!shipmentID.isNullOrBlank()) {
            val request = RequestUpdateShipment(
                shipmentStatus = "Delivered",
                expectedShipDate = shipDate,
                shippedAt = currentDateTime
            )
            updateShipmentViewModel.updateShipment(
                id = shipmentID!!,
                requestUpdateShipment = request
            )
        } else {
            shipmentStatus = "Delivered"
            markShipmentButtonCompleted()
            handleUpdateOrderShipment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlePaymentComplete() {
        Log.d("ORDER_DEBUG", "Complete Payment clicked")

        val currentDateTime = Instant.now().toString()
        val amountToPay = paymentAmount ?: orderDetail?.data?.totalPrice ?: 0.0

        if (!paymentID.isNullOrBlank()) {
            val request = RequestUpdatePayment(
                amount = amountToPay,
                paidAt = currentDateTime,
                paymentStatus = "Paid",
                transactionId = ""
            )
            updatePaymentViewModel.updatePayment(
                paymentID!!,
                requestUpdatePayment = request
            )
        } else {
            paymentStatus = "Paid"
            markPaymentButtonCompleted()
            handleUpdateOrderPayment()
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
        storeOwnerByIdViewModel.getStoreByIdState.collectInLifecycle(viewLifecycleOwner) { storeOwnerState ->
            if (storeOwnerState.loading) {
                loading.show()
            }
            storeOwnerState.error?.let {
                loading.dismiss()
            }

            storeOwnerState.data?.let { data ->
                loading.dismiss()
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

    @SuppressLint("SetTextI18n")
    private fun orderDetailsObserver() {
        orderDetailsViewModel.orderDetailsState.collectInLifecycle(viewLifecycleOwner) { orderDetailsState ->
            if (orderDetailsState.loading) {
                loading.show()
                return@collectInLifecycle
            }

            orderDetailsState.error?.let {
                loading.dismiss()
                Log.e("ORDER_DEBUG", "orderDetailsObserver error: $it")
            }

            orderDetailsState.data?.let { orderItem ->
                Log.d("orderId", "orderDetailsObserver: $orderItem")
                loading.dismiss()
                if (orderItem.isNotEmpty()) {
                    val sum = orderItem.sumOf { it.subtotal }
                    val formattedTotal = if (sum % 1.0 == 0.0) {
                        sum.toLong().toString()
                    } else {
                        "%.2f".format(sum)
                    }
                    binding.tvTotal.text = "Total: ৳$formattedTotal"
                    orderDetailsAdapter = OrderDetailsAdapter(orderItem)
                    binding.recyclerProducts.adapter = orderDetailsAdapter
                }
            }
        }
    }

    private fun orderDetailsDemoObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            orderDetailsViewModel.orderDetailsStateDemo.collect { response ->
                if (response.error != null) {
                    loading.dismiss()
                }
                if (response.data != null) {
                    loading.dismiss()
                    orderDetail = response.data
                    val orderData = response.data.data
                    if (orderData?.paymentStatus == "Paid") {
                        paymentStatus = "Paid"
                        markPaymentButtonCompleted()
                    }
                    if (orderData?.shipmentStatus == "Delivered") {
                        shipmentStatus = "Delivered"
                        markShipmentButtonCompleted()
                    }
                    if (paymentAmount == null && orderData?.totalPrice != null) {
                        paymentAmount = orderData.totalPrice
                    }
                    if (expectedShipDate.isNullOrBlank() && !orderData?.expectedShipDate.isNullOrBlank()) {
                        expectedShipDate = orderData?.expectedShipDate
                    }
                }
            }
        }
    }

    private fun getPaymentObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            getPaymentViewModel.getPaymentState.collect { response ->
                if (response.data != null) {
                    val paymentList = response.data.data
                    // Prefer a Paid payment if multiple records exist
                    val matchedPayment = paymentList?.firstOrNull { it?.paymentStatus == "Paid" }
                        ?: paymentList?.firstOrNull()

                    paymentID = matchedPayment?.id
                    paymentAmount = matchedPayment?.amount
                    if (matchedPayment?.paymentStatus == "Paid") {
                        paymentStatus = "Paid"
                        markPaymentButtonCompleted()
                    } else if (paymentStatus == null) {
                        paymentStatus = matchedPayment?.paymentStatus
                    }
                }
            }
        }
    }

    private fun updatePaymentObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            updatePaymentViewModel.updatePaymentState.collect { response ->
                if (response.loading) {
                    loading.show()
                }
                if (response.error != null) {
                    loading.dismiss()
                    Log.d("TAG", "update Payment error: ${response.error}")
                }
                if (response.data != null) {
                    loading.dismiss()
                    paymentStatus = response.data.data?.paymentStatus

                    if (response.data.data?.paymentStatus == "Paid") {
                        markPaymentButtonCompleted()
                        Toast.makeText(
                            requireContext(),
                            "Payment completed successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        handleUpdateOrderPayment()
                    }
                }
            }
        }
    }

    private fun updateShipmentObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            updateShipmentViewModel.updateShipmentState.collect { response ->
                if (response.loading) {
                    loading.show()
                }
                if (response.error != null) {
                    loading.dismiss()
                }
                if (response.data != null) {
                    loading.dismiss()
                    shipmentStatus = response.data.data?.shipmentStatus

                    if (response.data.data?.shipmentStatus == "Delivered") {
                        markShipmentButtonCompleted()
                        Toast.makeText(
                            requireContext(),
                            "Shipment marked as delivered",
                            Toast.LENGTH_SHORT
                        ).show()
                        handleUpdateOrderShipment()
                    }
                }
            }
        }
    }

    private fun getShipmentObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            getShipmentViewModel.getShipmentState.collect { response ->
                if (response.data != null) {
                    val shipmentList = response.data.data
                    val matchedShipment = shipmentList?.firstOrNull { it?.shipmentStatus == "Delivered" }
                        ?: shipmentList?.firstOrNull()

                    shipmentID = matchedShipment?.id
                    expectedShipDate = matchedShipment?.expectedShipDate
                    if (matchedShipment?.shipmentStatus == "Delivered") {
                        shipmentStatus = "Delivered"
                        markShipmentButtonCompleted()
                    } else if (shipmentStatus == null) {
                        shipmentStatus = matchedShipment?.shipmentStatus
                    }
                }
            }
        }
    }

    private fun updateOrderObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            updateOrderViewModel.updateOrderState.collect { response ->
                if (response.loading) {
                    loading.show()
                }
                if (response.error != null) {
                    loading.dismiss()
                    Log.d("TAG", "update order error: ${response.error}")
                }
                if (response.data != null) {
                    loading.dismiss()
                    Log.d("TAG", "update order success: ${response.data}")
                }
            }
        }
    }

    private fun handleUpdateOrderPayment() {
        val requestUpdateOrder = RequestUpdateOrder(
            paymentStatus = "Paid",
            amountPaid = paymentAmount ?: orderDetail?.data?.totalPrice,
            paymentDue = 0.0
        )
        if (!orderId.isNullOrBlank()) {
            updateOrderViewModel.updateOrder(
                id = orderId!!,
                requestUpdateOrder = requestUpdateOrder
            )
        }
    }

    private fun handleUpdateOrderShipment() {
        val requestUpdateOrder = RequestUpdateOrder(
            shipmentStatus = "Delivered"
        )
        if (!orderId.isNullOrBlank()) {
            updateOrderViewModel.updateOrder(
                id = orderId!!,
                requestUpdateOrder = requestUpdateOrder
            )
        }
    }
}
