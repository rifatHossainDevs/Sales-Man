package com.wevx.dealershipmanagement.data.remote.order

import com.wevx.dealershipmanagement.data.dto.order.updateOrder.RequestUpdateOrder
import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.RequestUpdatePayment
import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponseGetPaymentDTO
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

    @GET("orders/get-current-day-orders-by-salesmanId-paymentStatus/{sellerId}/{paymentStatus}")
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
