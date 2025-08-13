package com.wevx.dealershipmanagement.presentation.order.createOrder

import com.wevx.dealershipmanagement.data.createOrderDto.ResponseCreateOrderDTO
import com.wevx.dealershipmanagement.domain.models.PendingOrderModel

data class CreateOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseCreateOrderDTO? = null
)
