package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.data.dto.orderDetailsDTO.ResponseOderDetailsDTO
import com.wevx.dealershipmanagement.domain.models.OrderDetailsModel

data class OrderDetailsDataStateDEMO(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseOderDetailsDTO? = null
)
