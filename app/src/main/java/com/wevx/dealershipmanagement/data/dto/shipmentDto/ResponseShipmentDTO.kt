package com.wevx.dealershipmanagement.data.dto.shipmentDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class ResponseShipmentDTO(
    @SerializedName("data")
    val `data`: Data? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("success")
    val success: Boolean? = null
){
    @Keep
    @Serializable
    data class Data(
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("expectedShipDate")
        val expectedShipDate: String? = null,
        @SerializedName("_id")
        val id: String? = null,
        @SerializedName("orderId")
        val orderId: String? = null,
        @SerializedName("shipmentStatus")
        val shipmentStatus: String? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    )
}