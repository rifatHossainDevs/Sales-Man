package com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestPendingAndCompleteOrder(
    @SerializedName("paymentStatus")
    val paymentStatus: String? = null
)