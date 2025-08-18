package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.data.dto.ResponseGetPaymentDTO

data class GetPaymentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseGetPaymentDTO? = null
)
