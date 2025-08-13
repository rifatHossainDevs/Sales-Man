package com.wevx.dealershipmanagement.data.repository_impl.order

import com.wevx.dealershipmanagement.data.dto.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponsePaymentDTO
import com.wevx.dealershipmanagement.data.dto.pendingOrderDto.ResponsePendingOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO
import com.wevx.dealershipmanagement.data.remote.order.OrderApiService
import com.wevx.dealershipmanagement.domain.repository.order.OrderRepository
import jakarta.inject.Inject
import retrofit2.Response

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService
): OrderRepository {
    override suspend fun getPendingOrdersByCustomer(customerId: String): Response<ResponsePendingOrderDTO> {
        return orderApiService.getPendingOrderByCustomer(customerId)
    }

    override suspend fun createOrder(requestCreateOrderDTO: RequestCreateOrderDTO, token: String): Response<ResponseCreateOrderDTO> {
        return orderApiService.createOrder(requestCreateOrderDTO, token)
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


}