package com.wevx.dealershipmanagement.presentation.order.completeOrder

import com.wevx.dealershipmanagement.presentation.order.pendingOrder.PendingOrderDataState

data class CompleteDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<PendingOrderDataState>? = null
)
