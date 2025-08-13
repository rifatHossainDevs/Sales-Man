package com.wevx.dealershipmanagement.data.dto.createOrderDto

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestCreateOrderDTO(
    @SerializedName("amountPaid")
    val amountPaid: Double? = null,
    @SerializedName("customerId")
    val customerId: String? = null,
    @SerializedName("expectedShipDate")
    val expectedShipDate: String? = null,
    @SerializedName("invoiceNumber")
    val invoiceNumber: String? = null,
    @SerializedName("orderItems")
    val orderItems: List<OrderItem?>? = null,
    @SerializedName("paymentDue")
    val paymentDue: Double? = null,
    @SerializedName("salesmanId")
    val salesmanId: String? = null,
    @SerializedName("shippingAddress")
    val shippingAddress: String? = null,
    @SerializedName("totalPrice")
    val totalPrice: Double? = null
){
    @Keep
    @Serializable
    data class OrderItem(
        @SerializedName("priceAtPurchase")
        val priceAtPurchase: Double? = null,
        @SerializedName("productId")
        val productId: String? = null,
        @SerializedName("purchaseQuantity")
        val purchaseQuantity: Int? = null
    )
}