package com.wevx.dealershipmanagement.data.dto.shipmentDto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RequestUpdateShipment(
    @SerializedName("expectedShipDate")
    val expectedShipDate: String?,
    @SerializedName("shipmentStatus")
    val shipmentStatus: String?,
    @SerializedName("shippedAt")
    val shippedAt: String?
)