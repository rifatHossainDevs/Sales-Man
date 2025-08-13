package com.wevx.dealershipmanagement.data.dto.shipmentDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestShipmentDTO(
    @SerializedName("expectedShipDate")
    val expectedShipDate: String? = null,
    @SerializedName("orderId")
    val orderId: String? = null,
    @SerializedName("shipmentStatus")
    val shipmentStatus: String? = null
)