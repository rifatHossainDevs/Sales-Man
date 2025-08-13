package com.wevx.dealershipmanagement.presentation.order.createOrder

import com.wevx.dealershipmanagement.data.dto.createOrderDto.ResponseCreateOrderDTO

data class CreateOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseCreateOrderDTO? = null
)
