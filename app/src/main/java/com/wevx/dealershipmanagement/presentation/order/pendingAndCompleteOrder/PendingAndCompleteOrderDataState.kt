package com.wevx.dealershipmanagement.presentation.order.pendingAndCompleteOrder

import com.wevx.dealershipmanagement.domain.models.PendingAndCompleteOrderModel

data class PendingAndCompleteOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<PendingAndCompleteOrderModel>? = null
)
