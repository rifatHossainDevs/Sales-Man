package com.wevx.dealershipmanagement.presentation.orderDetails

import com.wevx.dealershipmanagement.data.dto.shipmentDto.ResponseGetShipmentByOrderDTO

data class GetShipmentDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseGetShipmentByOrderDTO? = null
)
