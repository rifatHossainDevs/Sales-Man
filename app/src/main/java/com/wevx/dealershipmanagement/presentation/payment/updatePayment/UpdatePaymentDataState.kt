package com.wevx.dealershipmanagement.presentation.payment.updatePayment

import com.wevx.dealershipmanagement.data.dto.paymentDto.updatePayment.ResponseUpdatePaymentDTO

data class UpdatePaymentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseUpdatePaymentDTO? = null
)
