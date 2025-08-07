package com.wevx.dealershipmanagement.data.remote.order

import com.wevx.dealershipmanagement.data.dto.pendingOrderDto.ResponsePendingOrderDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OrderApiService {

    @GET("orders/get-pending-orders-by-customer/{customerId}")
    suspend fun getPendingOrderByCustomer(@Path("customerId") customerId: String): Response<ResponsePendingOrderDTO>


}
