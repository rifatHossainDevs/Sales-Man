package com.wevx.dealershipmanagement.data.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RequestUpdatePayment(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("paidAt")
    val paidAt: String?,
    @SerializedName("paymentStatus")
    val paymentStatus: String?,
    @SerializedName("transactionId")
    val transactionId: String?
)