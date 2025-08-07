package com.wevx.dealershipmanagement.domain.repository.order

import com.wevx.dealershipmanagement.data.dto.pendingOrderDto.ResponsePendingOrderDTO
import retrofit2.Response

interface OrderRepository {

    suspend fun getPendingOrdersByCustomer(customerId: String): Response<ResponsePendingOrderDTO>



}