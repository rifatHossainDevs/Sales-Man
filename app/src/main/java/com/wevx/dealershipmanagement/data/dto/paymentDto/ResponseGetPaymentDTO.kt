package com.wevx.dealershipmanagement.data.dto.paymentDto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseGetPaymentDTO(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("statusCode")
    val statusCode: Int?,
    @SerializedName("success")
    val success: Boolean?
){
    @Keep
    data class Data(
        @SerializedName("amount")
        val amount: Int?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("orderId")
        val orderId: String?,
        @SerializedName("paidAt")
        val paidAt: String?,
        @SerializedName("paymentMethod")
        val paymentMethod: String?,
        @SerializedName("paymentStatus")
        val paymentStatus: String?,
        @SerializedName("salesmanId")
        val salesmanId: String?,
        @SerializedName("transactionId")
        val transactionId: String?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}