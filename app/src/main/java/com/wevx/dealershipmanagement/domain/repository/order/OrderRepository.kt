package com.wevx.dealershipmanagement.domain.repository.order

import com.wevx.dealershipmanagement.data.dto.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponsePaymentDTO
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.RequestPendingAndCompleteOrder
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.ResponsePendingAndCompleteOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO
import retrofit2.Response

interface OrderRepository {

    suspend fun getPendingAndCompleteOrderByCustomer(customerId: String, requestPendingAndCompleteOrder: RequestPendingAndCompleteOrder): Response<ResponsePendingAndCompleteOrderDTO>
    suspend fun createOrder(requestCreateOrderDTO: RequestCreateOrderDTO, token: String): Response<ResponseCreateOrderDTO>

    suspend fun createPayment(requestPayment: RequestPaymentDTO, token: String): Response<ResponsePaymentDTO>

    suspend fun createShipment(requestShipmentDTO: RequestShipmentDTO, token: String): Response<ResponseShipmentDTO>


}