package com.wevx.dealershipmanagement.domain.repository.order

import com.wevx.dealershipmanagement.data.dto.RequestUpdateOrder
import com.wevx.dealershipmanagement.data.dto.RequestUpdatePayment
import com.wevx.dealershipmanagement.data.dto.ResponseGetPaymentDTO
import com.wevx.dealershipmanagement.data.dto.ResponseUpdateOrderDTO
import com.wevx.dealershipmanagement.data.dto.ResponseUpdatePaymentDTO
import com.wevx.dealershipmanagement.data.dto.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.orderDetailsDTO.ResponseOderDetailsDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponsePaymentDTO
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.ResponsePendingAndCompleteOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestUpdateShipment
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseGetShipmentByOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseUpdateShipmentDTO
import com.wevx.dealershipmanagement.data.dto.todaysDelivery.ResponseTodaysDelivery
import retrofit2.Response

interface OrderRepository {

    suspend fun getPendingOrderByCustomer(
        customerId: String,
        paymentStatus: String
    ): Response<ResponsePendingAndCompleteOrderDTO>

    suspend fun getCompleteOrderByCustomer(
        customerId: String,
        paymentStatus: String
    ): Response<ResponsePendingAndCompleteOrderDTO>

    suspend fun createOrder(
        requestCreateOrderDTO: RequestCreateOrderDTO,
        token: String
    ): Response<ResponseCreateOrderDTO>

    suspend fun updateOrder(
        id: String,
        requestUpdateOrder: RequestUpdateOrder
    ): Response<ResponseUpdateOrderDTO>

    suspend fun createPayment(
        requestPayment: RequestPaymentDTO,
        token: String
    ): Response<ResponsePaymentDTO>

    suspend fun createShipment(
        requestShipmentDTO: RequestShipmentDTO,
        token: String
    ): Response<ResponseShipmentDTO>

    suspend fun getShipmentByOrderId(id: String): Response<ResponseGetShipmentByOrderDTO>

    suspend fun getOrderById(orderId: String): Response<ResponseOderDetailsDTO>

    suspend fun getSellerPendingOrder(
        sellerId: String,
        paymentStatus: String
    ): Response<ResponseTodaysDelivery>

    suspend fun getPaymentByOrderID(id: String): Response<ResponseGetPaymentDTO>
    suspend fun updatePayment(
        id: String,
        requestUpdatePayment: RequestUpdatePayment
    ): Response<ResponseUpdatePaymentDTO>

    suspend fun updateShipment(
        id: String,
        requestUpdateShipment: RequestUpdateShipment
    ): Response<ResponseUpdateShipmentDTO>


}