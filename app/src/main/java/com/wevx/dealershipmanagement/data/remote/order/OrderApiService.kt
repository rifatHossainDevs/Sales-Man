package com.wevx.dealershipmanagement.data.remote.order

import com.wevx.dealershipmanagement.data.dto.ResponseGetPaymentDTO
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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {

    @GET("orders/get-orders-by-customer-paymentStatus/{customerId}/{paymentStatus}")
    suspend fun getPendingOrderByCustomer(
        @Path("customerId") customerId: String,
        @Path("paymentStatus") paymentStatus: String
    ): Response<ResponsePendingAndCompleteOrderDTO>

    @GET("orders/get-orders-by-customer-paymentStatus/{customerId}/{paymentStatus}")
    suspend fun getCompleteOrderByCustomer(
        @Path("customerId") customerId: String,
        @Path("paymentStatus") paymentStatus: String
    ): Response<ResponsePendingAndCompleteOrderDTO>

    @GET("orders/get-orders-by-customer-paymentStatus/{sellerId}/{paymentStatus}")
    suspend fun getSellerPendingOrder(
        @Path("sellerId") sellerId: String,
        @Path("paymentStatus") paymentStatus: String
    ): Response<ResponseTodaysDelivery>

    @POST("orders/create-order")
    suspend fun createOrder(
        @Body requestCreateOrderDTO: RequestCreateOrderDTO,
        @Header("Authorization") token: String
    ): Response<ResponseCreateOrderDTO>

    @POST("payments/create-payment")
    suspend fun createPayment(
        @Body requestPayment: RequestPaymentDTO,
        @Header("Authorization") token: String
    ): Response<ResponsePaymentDTO>


    @POST("shipments/create-shipment")
    suspend fun createShipment(
        @Body requestShipment: RequestShipmentDTO,
        @Header("Authorization") token: String
    ): Response<ResponseShipmentDTO>

    @GET("orders/get-order/{orderId}")
    suspend fun getOrderById(
        @Path("orderId") orderId: String
    ): Response<ResponseOderDetailsDTO>


    @POST("payments/create-payment")
    suspend fun getPaymentById(
        @Path("id") id: String
    ): Response<ResponseGetPaymentDTO>

}
