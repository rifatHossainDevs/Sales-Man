package com.wevx.dealershipmanagement.data.remote.order

import com.wevx.dealershipmanagement.data.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.data.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.data.dto.pendingOrderDto.ResponsePendingOrderDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {

    @GET("orders/get-pending-orders-by-customer/{customerId}")
    suspend fun getPendingOrderByCustomer(@Path("customerId") customerId: String): Response<ResponsePendingOrderDTO>

    @POST("orders/create-order")
    suspend fun createOrder(@Body requestCreateOrderDTO: RequestCreateOrderDTO, @Header("Authorization") token: String): Response<ResponseCreateOrderDTO>


}
