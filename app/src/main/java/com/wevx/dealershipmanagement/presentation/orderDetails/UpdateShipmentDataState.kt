package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseUpdateShipmentDTO

data class UpdateShipmentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseUpdateShipmentDTO? = null
)
