
package com.wevx.dealershipmanagement.data.dto.order.createOrderDto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ResponseCreateOrderDTO(
    @SerializedName("data")
    val data: Data? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("statusCode")
    val statusCode: Int? = null,

    @SerializedName("success")
    val success: Boolean? = null
) {
    @Keep
    @Serializable
    data class Data(
        @SerializedName("amountPaid")
        val amountPaid: Double? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("customerId")
        val customerId: String? = null,

        @SerializedName("expectedShipDate")
        val expectedShipDate: String? = null,

        @SerializedName("_id")
        val id: String? = null,

        @SerializedName("invoiceNumber")
        val invoiceNumber: String? = null,

        @SerializedName("orderItems")
        val orderItems: List<OrderItem?>? = null,

        @SerializedName("paymentDue")
        val paymentDue: Double? = null,

        @SerializedName("paymentStatus")
        val paymentStatus: String? = null,

        @SerializedName("salesmanId")
        val salesmanId: String? = null,

        @SerializedName("shippingAddress")
        val shippingAddress: String? = null,

        @SerializedName("totalPrice")
        val totalPrice: Double? = null,

        @SerializedName("updatedAt")
        val updatedAt: String? = null,

        @SerializedName("__v")
        val v: Int? = null,

        @SerializedName("areaName")
        val areaName: String? = null,

        @SerializedName("areaNo")
        val areaNo: String? = null,

        @SerializedName("shipmentStatus")
        val shipmentStatus: String? = null
    ) {
        @Keep
        @Serializable
        data class OrderItem(
            @SerializedName("createdAt")
            val createdAt: String? = null,

            @SerializedName("_id")
            val id: String? = null,

            @SerializedName("priceAtPurchase")
            val priceAtPurchase: Double? = null,

            @SerializedName("productId")
            val productId: String? = null,

            @SerializedName("productName")
            val productName: String? = null,

            @SerializedName("purchaseQuantity")
            val purchaseQuantity: Int? = null,

            @SerializedName("updatedAt")
            val updatedAt: String? = null
        )
    }
}
