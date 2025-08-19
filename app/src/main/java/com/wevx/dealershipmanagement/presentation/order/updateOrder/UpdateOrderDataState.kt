package com.wevx.dealershipmanagement.presentation.order.updateOrder

import com.wevx.dealershipmanagement.data.dto.order.updateOrder.ResponseUpdateOrderDTO

data class UpdateOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseUpdateOrderDTO? = null
)
