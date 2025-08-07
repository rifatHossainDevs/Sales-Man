package com.wevx.dealershipmanagement.presentation.order.pendingOrder

import com.wevx.dealershipmanagement.domain.models.PendingOrderModel

data class PendingOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<PendingOrderModel>? = null
)
