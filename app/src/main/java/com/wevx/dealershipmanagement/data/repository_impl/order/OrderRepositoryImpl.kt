package com.wevx.dealershipmanagement.data.repository_impl.order

import com.wevx.dealershipmanagement.data.dto.order.updateOrder.RequestUpdateOrder
import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.RequestUpdatePayment
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponseGetPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponseMonthlyPaymentsDTO
import com.wevx.dealershipmanagement.data.dto.order.updateOrder.ResponseUpdateOrderDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.ResponseUpdatePaymentDTO
import com.wevx.dealershipmanagement.data.dto.order.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.order.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.order.orderDetailsDTO.ResponseOderDetailsDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.createPayment.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.createPayment.ResponsePaymentDTO
import com.wevx.dealershipmanagement.data.dto.order.pendingAndCompleteOrderDto.ResponsePendingAndCompleteOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestUpdateShipment
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseGetShipmentByOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseUpdateShipmentDTO
import com.wevx.dealershipmanagement.data.dto.order.todaysDelivery.ResponseTodaysDelivery
import com.wevx.dealershipmanagement.data.dto.order.ResponseSalesmanOrdersDTO
import com.wevx.dealershipmanagement.data.remote.order.OrderApiService
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import jakarta.inject.Inject
import retrofit2.Response

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService
) : OrderRepository {
    override suspend fun getPendingOrderByCustomer(
        customerId: String,
        paymentStatus: String
    ): Response<ResponsePendingAndCompleteOrderDTO> {
        return orderApiService.getPendingOrderByCustomer(customerId, paymentStatus)
    }

    override suspend fun getCompleteOrderByCustomer(
        customerId: String,
        paymentStatus: String
    ): Response<ResponsePendingAndCompleteOrderDTO> {
        return orderApiService.getCompleteOrderByCustomer(customerId, paymentStatus)
    }


    override suspend fun createOrder(
        requestCreateOrderDTO: RequestCreateOrderDTO,
        token: String
    ): Response<ResponseCreateOrderDTO> {
        return orderApiService.createOrder(requestCreateOrderDTO, token)
    }

    override suspend fun updateOrder(
        id: String,
        requestUpdateOrder: RequestUpdateOrder
    ): Response<ResponseUpdateOrderDTO> {
        return if (!requestUpdateOrder.shipmentStatus.isNullOrBlank() && requestUpdateOrder.paymentStatus.isNullOrBlank()) {
            orderApiService.updateOrderShipmentStatus(
                id = id,
                requestUpdateOrder = requestUpdateOrder
            )
        } else {
            orderApiService.updateOrder(
                id = id,
                requestUpdateOrder = requestUpdateOrder
            )
        }
    }

    override suspend fun createPayment(
        requestPayment: RequestPaymentDTO,
        token: String
    ): Response<ResponsePaymentDTO> {
        return orderApiService.createPayment(requestPayment, token)

    }

    override suspend fun createShipment(
        requestShipmentDTO: RequestShipmentDTO,
        token: String
    ): Response<ResponseShipmentDTO> {
        return orderApiService.createShipment(requestShipmentDTO, token)

    }

    override suspend fun getShipmentByOrderId(id: String): Response<ResponseGetShipmentByOrderDTO> {
        return orderApiService.getShipmentByOrderId(id)
    }

    override suspend fun getOrderById(orderId: String): Response<ResponseOderDetailsDTO> {
        return orderApiService.getOrderById(orderId)
    }

    override suspend fun getSellerPendingOrder(
        sellerId: String,
        paymentStatus: String
    ): Response<ResponseTodaysDelivery> {
        return orderApiService.getSellerPendingOrder(sellerId, paymentStatus)
    }

    override suspend fun getPendingPaymentOrdersBySalesman(
        sellerId: String
    ): Response<ResponseTodaysDelivery> {
        return orderApiService.getPendingPaymentOrdersBySalesman(sellerId)
    }

    override suspend fun getOrdersByCustomer(
        customerId: String
    ): Response<ResponseTodaysDelivery> {
        return orderApiService.getOrdersByCustomer(customerId)
    }

    override suspend fun getAllStoresForDelivery(): Response<com.google.gson.JsonObject> {
        return orderApiService.getAllStoresForDelivery()
    }

    override suspend fun getPaymentByOrderID(id: String): Response<ResponseGetPaymentDTO> {
        return orderApiService.getPaymentByOrderId(id)
    }

    override suspend fun updatePayment(id: String, requestUpdatePayment: RequestUpdatePayment): Response<ResponseUpdatePaymentDTO> {
        return orderApiService.updatePayment(paymentId = id, requestUpdatePayment = requestUpdatePayment)
    }

    override suspend fun updateShipment(
        id: String,
        requestUpdateShipment: RequestUpdateShipment
    ): Response<ResponseUpdateShipmentDTO> {
        return orderApiService.updateShipment(id = id, requestUpdateShipment = requestUpdateShipment)
    }

    override suspend fun getMonthlyTotalPayments(
        salesmanId: String,
        month: String,
        paymentStatus: String
    ): Response<ResponseMonthlyPaymentsDTO> {
        return orderApiService.getMonthlyTotalPayments(salesmanId, month, paymentStatus)
    }

    override suspend fun getOrdersBySalesman(
        salesmanId: String,
        limit: Int
    ): Response<ResponseSalesmanOrdersDTO> {
        return orderApiService.getOrdersBySalesman(salesmanId, limit)
    }
}