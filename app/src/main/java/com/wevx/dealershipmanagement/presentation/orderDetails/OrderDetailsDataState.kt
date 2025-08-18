package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.domain.models.OrderDetailsModel

data class OrderDetailsDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<OrderDetailsModel>? = null
)
