package com.wevx.dealershipmanagement.presentation.order.pendingOrder

import com.wevx.dealershipmanagement.domain.models.PendingAndCompleteOrderModel

data class PendingOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<PendingAndCompleteOrderModel>? = null
)
