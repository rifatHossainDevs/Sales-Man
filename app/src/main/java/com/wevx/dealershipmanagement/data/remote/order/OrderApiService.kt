package com.wevx.dealershipmanagement.data.remote.order

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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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

    @PATCH("orders/update-paymentStatus/{id}")
    suspend fun updateOrder(
        @Path("id") id: String,
        @Body requestUpdateOrder: RequestUpdateOrder
    ): Response<ResponseUpdateOrderDTO>

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


    @GET("payments/get-payments-by-order/{orderId}")
    suspend fun getPaymentByOrderId(
        @Path("orderId") orderId: String
    ): Response<ResponseGetPaymentDTO>


    @PATCH("payments/update-payment/{id}")
    suspend fun updatePayment(
        @Path("id") id: String,
        @Body requestUpdatePayment: RequestUpdatePayment
    ): Response<ResponseUpdatePaymentDTO>

    @PATCH("shipments/update-shipment/{id}")
    suspend fun updateShipment(
        @Path("id") id: String,
        @Body requestUpdateShipment: RequestUpdateShipment
    ): Response<ResponseUpdateShipmentDTO>

    @GET("shipments/get-shipments-by-order/{id}")
    suspend fun getShipmentByOrderId(
        @Path("id") id: String,
    ): Response<ResponseGetShipmentByOrderDTO>


}
