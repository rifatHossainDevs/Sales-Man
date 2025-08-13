package com.wevx.dealershipmanagement.data.dto.paymentDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class ResponsePaymentDTO(
    @SerialName("data")
    val `data`: Data? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("statusCode")
    val statusCode: Int? = null,
    @SerialName("success")
    val success: Boolean? = null
){
    @Keep
    @Serializable
    data class Data(
        @SerialName("amount")
        val amount: Int? = null,
        @SerialName("createdAt")
        val createdAt: String? = null,
        @SerialName("_id")
        val id: String? = null,
        @SerialName("orderId")
        val orderId: String? = null,
        @SerialName("paidAt")
        val paidAt: String? = null,
        @SerialName("paymentMethod")
        val paymentMethod: String? = null,
        @SerialName("paymentStatus")
        val paymentStatus: String? = null,
        @SerialName("salesmanId")
        val salesmanId: String? = null,
        @SerialName("transactionId")
        val transactionId: String? = null,
        @SerialName("updatedAt")
        val updatedAt: String? = null,
        @SerialName("__v")
        val v: Int? = null
    )
}