package com.wevx.dealershipmanagement.presentation.order.createPayment

import com.wevx.dealershipmanagement.data.dto.paymentDto.ResponsePaymentDTO

data class CreatePaymentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponsePaymentDTO? = null
)
