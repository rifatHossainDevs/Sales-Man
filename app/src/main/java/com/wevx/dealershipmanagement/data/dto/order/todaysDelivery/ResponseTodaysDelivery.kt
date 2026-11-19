package com.wevx.dealershipmanagement.data.dto.order.todaysDelivery

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.wevx.dealershipmanagement.domain.models.PendingOrderSellerModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Keep
data class ResponseTodaysDelivery(
    @SerializedName("data")
    val `data`: List<Data?>? = null,
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
        @SerializedName("shipmentStatus")
        val shipmentStatus: String? = null,
        @SerializedName("salesmanId")
        val salesmanId: String? = null,
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
            @SerializedName("purchaseQuantity")
            val purchaseQuantity: Int? = null,
            @SerializedName("updatedAt")
            val updatedAt: String? = null
        )
    }
}

private fun formatAmount(value: Double?): String {
    if (value == null) return "0"
    return if (value % 1.0 == 0.0) value.toLong().toString() else "%.2f".format(value)
}

private fun formatOrderDate(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return ""
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd"
    )
    for (pattern in patterns) {
        try {
            val parser = SimpleDateFormat(pattern, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val parsed = parser.parse(isoDate) ?: continue
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return formatter.format(parsed)
        } catch (_: Exception) {
        }
    }
    return isoDate.take(10)
}

fun ResponseTodaysDelivery.Data.toTodaysDeliveryModel(): PendingOrderSellerModel {
    val payStat = this.paymentStatus ?: "Pending"
    val shipStat = this.shipmentStatus ?: "Pending"
    return PendingOrderSellerModel(
        id = this.id ?: "",
        invoiceNumber = this.invoiceNumber ?: "",
        address = this.shippingAddress ?: "",
        paymentStatus = "$payStat · Delivery: $shipStat",
        total = formatAmount(this.totalPrice),
        customerId = this.customerId ?: "",
        orderDate = formatOrderDate(this.expectedShipDate ?: this.createdAt)
    )
}

fun List<ResponseTodaysDelivery.Data?>?.toPendingOrderSellerModel(): List<PendingOrderSellerModel> {
    return this?.filterNotNull()
        ?.filter { it.shipmentStatus != "Delivered" && it.shipmentStatus != "Failed" }
        ?.sortedByDescending { it.expectedShipDate ?: it.createdAt ?: "" }
        ?.map { it.toTodaysDeliveryModel() }
        ?: emptyList()
}

fun List<ResponseTodaysDelivery.Data?>?.toPendingPaymentOrderSellerModel(): List<PendingOrderSellerModel> {
    return this?.filterNotNull()
        ?.filter { it.paymentStatus == "Pending" && it.shipmentStatus != "Failed" }
        ?.sortedByDescending { it.createdAt ?: "" }
        ?.map { item ->
            item.toTodaysDeliveryModel().copy(
                orderDate = formatOrderDate(item.createdAt)
            )
        }
        ?: emptyList()
}
