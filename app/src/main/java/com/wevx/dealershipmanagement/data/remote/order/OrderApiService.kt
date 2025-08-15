package com.wevx.dealershipmanagement.data.remote.order

import com.wevx.dealershipmanagement.data.dto.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponsePaymentDTO
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.RequestPendingAndCompleteOrder
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.ResponsePendingAndCompleteOrderDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {

    @GET("orders/get-orders-by-customer-paymentStatus/{customerId}")
    suspend fun getPendingAndCompleteOrderByCustomer(@Path("customerId") customerId: String, @Body requestPendingAndCompleteOrder: RequestPendingAndCompleteOrder): Response<ResponsePendingAndCompleteOrderDTO>

    @POST("orders/create-order")
    suspend fun createOrder(@Body requestCreateOrderDTO: RequestCreateOrderDTO, @Header("Authorization") token: String): Response<ResponseCreateOrderDTO>

    @POST("payments/create-payment")
    suspend fun createPayment(@Body requestPayment: RequestPaymentDTO, @Header("Authorization") token: String): Response<ResponsePaymentDTO>


    @POST("shipments/create-shipment")
    suspend fun createShipment(@Body requestShipment: RequestShipmentDTO, @Header("Authorization") token: String): Response<ResponseShipmentDTO>

}
