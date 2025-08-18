package com.wevx.dealershipmanagement.data.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RequestUpdateOrder(
    @SerializedName("paymentStatus")
    val paymentStatus: String?,
)