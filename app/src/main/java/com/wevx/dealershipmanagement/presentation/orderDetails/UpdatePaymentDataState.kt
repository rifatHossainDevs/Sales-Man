package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.data.dto.ResponseUpdatePaymentDTO

data class UpdatePaymentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseUpdatePaymentDTO? = null
)
