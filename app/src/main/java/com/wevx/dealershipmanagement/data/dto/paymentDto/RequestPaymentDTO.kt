package com.wevx.dealershipmanagement.data.dto.paymentDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestPaymentDTO(
    @SerializedName("amount")
    val amount: Double? = null,
    @SerializedName("orderId")
    val orderId: String? = null,
    @SerializedName("paymentMethod")
    val paymentMethod: String? = null,
    @SerializedName("paymentStatus")
    val paymentStatus: String? = null,
    @SerializedName("salesmanId")
    val salesmanId: String? = null
)