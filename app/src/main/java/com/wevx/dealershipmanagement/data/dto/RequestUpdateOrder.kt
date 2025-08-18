package com.wevx.dealershipmanagement.data.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RequestUpdateOrder(
    @SerializedName("amountPaid")
    val amountPaid: Double?,
    @SerializedName("expectedShipDate")
    val expectedShipDate: String?,
    @SerializedName("orderItems")
    val orderItems: List<OrderItem?>?,
    @SerializedName("paymentDue")
    val paymentDue: Double?,
    @SerializedName("paymentStatus")
    val paymentStatus: String?,
    @SerializedName("shippingAddress")
    val shippingAddress: String?,
    @SerializedName("totalPrice")
    val totalPrice: Double?
){
    @Keep
    data class OrderItem(
        @SerializedName("priceAtPurchase")
        val priceAtPurchase: Double?,
        @SerializedName("productId")
        val productId: String?,
        @SerializedName("purchaseQuantity")
        val purchaseQuantity: Int?
    )
}