package com.wevx.dealershipmanagement.data.dto.paymentDto.createPayment

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponsePaymentDTO(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null
) {
    @Keep
    data class Data(
        @SerializedName("amount")
        val amount: Double? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("orderId")
        val orderId: String? = null,
        @SerializedName("paidAt")
        val paidAt: String? = null,
        @SerializedName("paymentMethod")
        val paymentMethod: String? = null,
        @SerializedName("paymentStatus")
        val paymentStatus: String? = null,
        @SerializedName("salesmanId")
        val salesmanId: String? = null,
        @SerializedName("transactionId")
        val transactionId: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}
