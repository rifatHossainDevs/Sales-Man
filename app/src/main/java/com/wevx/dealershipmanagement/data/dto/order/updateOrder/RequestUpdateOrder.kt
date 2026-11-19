package com.wevx.dealershipmanagement.data.dto.order.updateOrder


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RequestUpdateOrder(
    @SerializedName("paymentStatus")
    val paymentStatus: String? = null,
    @SerializedName("shipmentStatus")
    val shipmentStatus: String? = null,
    @SerializedName("amountPaid")
    val amountPaid: Double? = null,
    @SerializedName("paymentDue")
    val paymentDue: Double? = null,
)
