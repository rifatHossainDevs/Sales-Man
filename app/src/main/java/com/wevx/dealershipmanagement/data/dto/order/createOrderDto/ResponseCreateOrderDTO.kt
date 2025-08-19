package com.wevx.dealershipmanagement.data.dto.order.createOrderDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class ResponseCreateOrderDTO(
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
        @SerializedName("amountPaid")
        val amountPaid: Int? = null,
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
        val paymentDue: Int? = null,
        @SerializedName("paymentStatus")
        val paymentStatus: String? = null,
        @SerializedName("salesmanId")
        val salesmanId: String? = null,
        @SerializedName("shippingAddress")
        val shippingAddress: String? = null,
        @SerializedName("totalPrice")
        val totalPrice: Int? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    ){
        @Keep
        @Serializable
        data class OrderItem(
            @SerializedName("createdAt")
            val createdAt: String? = null,
            @SerializedName("_id")
            val id: String? = null,
            @SerializedName("priceAtPurchase")
            val priceAtPurchase: Int? = null,
            @SerializedName("productId")
            val productId: String? = null,
            @SerializedName("purchaseQuantity")
            val purchaseQuantity: Int? = null,
            @SerializedName("updatedAt")
            val updatedAt: String? = null
        )
    }
}