package com.wevx.dealershipmanagement.presentation.order.createShipment

import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseShipmentDTO

data class CreateShipmentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseShipmentDTO? = null
)
