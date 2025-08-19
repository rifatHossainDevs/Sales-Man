package com.wevx.dealershipmanagement.presentation.payment.createPayment

import com.wevx.dealershipmanagement.data.dto.paymentDto.createPayment.ResponsePaymentDTO

data class CreatePaymentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponsePaymentDTO? = null
)
