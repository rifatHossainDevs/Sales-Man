package com.wevx.dealershipmanagement.presentation.order.completeOrder

import com.wevx.dealershipmanagement.domain.models.PendingAndCompleteOrderModel

data class CompleteOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<PendingAndCompleteOrderModel>? = null
)
