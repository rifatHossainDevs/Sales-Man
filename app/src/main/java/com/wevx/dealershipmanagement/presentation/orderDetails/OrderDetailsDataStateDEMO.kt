package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.data.dto.order.orderDetailsDTO.ResponseOderDetailsDTO

data class OrderDetailsDataStateDEMO(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseOderDetailsDTO? = null
)
