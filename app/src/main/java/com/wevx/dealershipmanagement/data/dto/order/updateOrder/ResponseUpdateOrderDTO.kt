package com.wevx.dealershipmanagement.data.dto.order.updateOrder


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ResponseUpdateOrderDTO(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("statusCode")
    val statusCode: Int?,
    @SerializedName("success")
    val success: Boolean?
){
    @Keep
    data class Data(
        @SerializedName("amountPaid")
        val amountPaid: Double?,
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("customerId")
        val customerId: Any?,
        @SerializedName("expectedShipDate")
        val expectedShipDate: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("invoiceNumber")
        val invoiceNumber: String?,
        @SerializedName("orderItems")
        val orderItems: List<OrderItem?>?,
        @SerializedName("paymentDue")
        val paymentDue: Double?,
        @SerializedName("paymentStatus")
        val paymentStatus: String?,
        @SerializedName("shipmentStatus")
        val shipmentStatus: String?,
        @SerializedName("salesmanId")
        val salesmanId: Any?,
        @SerializedName("shippingAddress")
        val shippingAddress: String?,
        @SerializedName("totalPrice")
        val totalPrice: Double?,
        @SerializedName("updatedAt")
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    ){
        @Keep
        data class OrderItem(
            @SerializedName("createdAt")
            val createdAt: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("priceAtPurchase")
            val priceAtPurchase: Double?,
            @SerializedName("productId")
            val productId: String?,
            @SerializedName("purchaseQuantity")
            val purchaseQuantity: Int?,
            @SerializedName("updatedAt")
            val updatedAt: String?
        )
    }
}
