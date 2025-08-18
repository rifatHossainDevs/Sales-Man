package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.data.dto.ResponseUpdateOrderDTO

data class UpdateOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseUpdateOrderDTO? = null
)
