package com.wevx.dealershipmanagement.data.dto.shipmentDto

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ResponseGetShipmentByOrderDTO(
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
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("expectedShipDate")
        val expectedShipDate: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("orderId")
        val orderId: String?,
        @SerializedName("shipmentStatus")
        val shipmentStatus: String?,
        @SerializedName("shippedAt")
        val shippedAt: Any?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}
