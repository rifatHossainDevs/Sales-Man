package com.wevx.dealershipmanagement.data.dto.order.orderDetailsDTO

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.OrderDetailsModel

@Keep
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
    data class Data(
        @SerializedName("amountPaid")
        val amountPaid: Double? = null,
        @SerializedName("createdAt")
        val createdAt: String? = null,
        @SerializedName("customerId")
        val customerId: Any? = null,
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
        @SerializedName("shipmentStatus")
        val shipmentStatus: String? = null,
        @SerializedName("salesmanId")
        val salesmanId: Any? = null,
        @SerializedName("shippingAddress")
        val shippingAddress: String? = null,
        @SerializedName("totalPrice")
        val totalPrice: Double? = null,
        @SerializedName("updatedAt")
        val updatedAt: String? = null,
        @SerializedName("__v")
        val v: Int? = null
    ) {
        @Keep
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

// Per-item totals (subtotal used via computed property, total from parent if needed)
fun ResponseOderDetailsDTO.Data.toOrderDetailsModelList(): List<OrderDetailsModel> {
    return this.orderItems
        ?.filterNotNull()
        ?.map { item ->
            val quantity = item.purchaseQuantity?.toDouble() ?: 0.0
            val price = item.priceAtPurchase ?: 0.0

            OrderDetailsModel(
                name = item.productName.orEmpty(),
                quantity = quantity,
                unit = "pcs",
                price = price,
                total = quantity * price
            )
        } ?: emptyList()
}

// Per-item mapping for RecyclerView (total = subtotal = qty * price)
fun ResponseOderDetailsDTO.Data.OrderItem.toOrderDetailsModel(): OrderDetailsModel {
    val quantity = this.purchaseQuantity?.toDouble() ?: 0.0
    val price = this.priceAtPurchase ?: 0.0

    return OrderDetailsModel(
        name = this.productName.orEmpty(),
        quantity = quantity,
        unit = "pcs",
        price = price,
        total = quantity * price
    )
}

fun List<ResponseOderDetailsDTO.Data.OrderItem?>?.toPendingOrderSellerModel(): List<OrderDetailsModel> {
    return this?.filterNotNull()?.map { it.toOrderDetailsModel() } ?: emptyList()
}
