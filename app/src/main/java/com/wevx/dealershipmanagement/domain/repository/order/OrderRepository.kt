package com.wevx.dealershipmanagement.domain.repository.order

import com.wevx.dealershipmanagement.data.dto.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.orderDetailsDTO.ResponseOderDetailsDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponsePaymentDTO
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.ResponsePendingAndCompleteOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO
import com.wevx.dealershipmanagement.data.dto.todaysDelivery.ResponseTodaysDelivery
import retrofit2.Response

interface OrderRepository {

    suspend fun getPendingOrderByCustomer(customerId: String, paymentStatus: String): Response<ResponsePendingAndCompleteOrderDTO>

    suspend fun getCompleteOrderByCustomer(customerId: String, paymentStatus: String): Response<ResponsePendingAndCompleteOrderDTO>
    suspend fun createOrder(requestCreateOrderDTO: RequestCreateOrderDTO, token: String): Response<ResponseCreateOrderDTO>

    suspend fun createPayment(requestPayment: RequestPaymentDTO, token: String): Response<ResponsePaymentDTO>

    suspend fun createShipment(requestShipmentDTO: RequestShipmentDTO, token: String): Response<ResponseShipmentDTO>

    suspend fun getOrderById(orderId: String): Response<ResponseOderDetailsDTO>

    suspend fun getSellerPendingOrder(sellerId: String, paymentStatus: String): Response<ResponseTodaysDelivery>


}