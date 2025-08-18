package com.wevx.dealershipmanagement.presentation.order.sellerPendingOrder

import com.wevx.dealershipmanagement.domain.models.PendingOrderSellerModel

data class SellerPendingOrderDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<PendingOrderSellerModel>? = null
)
