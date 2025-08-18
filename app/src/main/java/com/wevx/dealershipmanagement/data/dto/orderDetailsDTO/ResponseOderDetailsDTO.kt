package com.wevx.dealershipmanagement.data.dto.orderDetailsDTO

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.OrderDetailsModel

@Keep
@Serializable
data class ResponseOderDetailsDTO(
    @SerializedName("data")
    val `data`: Data? = null,
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
    ) {
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

// Per-item totals (subtotal used via computed property, total from parent if needed)
fun ResponseOderDetailsDTO.Data.toOrderDetailsModelList(): List<OrderDetailsModel> {
    val orderTotal = this.totalPrice?.toDouble() ?: 0.0
    return this.orderItems?.filterNotNull()?.map { item ->
        OrderDetailsModel(
            name = "",
            quantity = item.purchaseQuantity?.toDouble() ?: 0.0,
            unit = "pcs",
            price = item.priceAtPurchase?.toDouble() ?: 0.0,
            total = orderTotal   // grand total for the order
        )
    } ?: emptyList()
}

// Per-item mapping for RecyclerView (total = subtotal = qty * price)
fun ResponseOderDetailsDTO.Data.OrderItem.toOrderDetailsModel(): OrderDetailsModel {
    val quantity = this.purchaseQuantity?.toDouble() ?: 0.0
    val price = this.priceAtPurchase?.toDouble() ?: 0.0
    return OrderDetailsModel(
        name = "",
        quantity = quantity,
        unit = "pcs",
        price = price,
        total = quantity * price  // per-item total
    )
}

fun List<ResponseOderDetailsDTO.Data.OrderItem?>?.toPendingOrderSellerModel(): List<OrderDetailsModel> {
    return this?.filterNotNull()?.map { it.toOrderDetailsModel() } ?: emptyList()
}
