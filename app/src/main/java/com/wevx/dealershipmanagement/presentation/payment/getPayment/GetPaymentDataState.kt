package com.wevx.dealershipmanagement.presentation.payment.getPayment

import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponseGetPaymentDTO

data class GetPaymentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseGetPaymentDTO? = null
)
