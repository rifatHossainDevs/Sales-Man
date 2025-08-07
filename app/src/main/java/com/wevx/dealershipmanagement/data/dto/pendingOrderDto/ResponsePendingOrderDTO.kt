package com.wevx.dealershipmanagement.data.dto.pendingOrderDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.wevx.dealershipmanagement.data.dto.productDto.ResponseProductDTO
import com.wevx.dealershipmanagement.data.dto.productDto.toProductModel
import com.wevx.dealershipmanagement.domain.models.PendingOrderModel
import com.wevx.dealershipmanagement.domain.models.ProductModel

@Keep
@Serializable
data class ResponsePendingOrderDTO(
    @SerialName("data")
    val `data`: List<Data?>? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("statusCode")
    val statusCode: Int? = null,
    @SerialName("success")
    val success: Boolean? = null
) {
    @Keep
    @Serializable
    data class Data(
        @SerialName("amountPaid")
        val amountPaid: Int? = null,
        @SerialName("createdAt")
        val createdAt: String? = null,
        @SerialName("customerId")
        val customerId: String? = null,
        @SerialName("expectedShipDate")
        val expectedShipDate: String? = null,
        @SerialName("_id")
        val id: String? = null,
        @SerialName("invoiceNumber")
        val invoiceNumber: String? = null,
        @SerialName("orderItems")
        val orderItems: List<OrderItem?>? = null,
        @SerialName("paymentDue")
        val paymentDue: Int? = null,
        @SerialName("paymentStatus")
        val paymentStatus: String? = null,
        @SerialName("salesmanId")
        val salesmanId: String? = null,
        @SerialName("shippingAddress")
        val shippingAddress: String? = null,
        @SerialName("totalPrice")
        val totalPrice: Int? = null,
        @SerialName("updatedAt")
        val updatedAt: String? = null,
        @SerialName("__v")
        val v: Int? = null
    ) {
        @Keep
        @Serializable
        data class OrderItem(
            @SerialName("createdAt")
            val createdAt: String? = null,
            @SerialName("_id")
            val id: String? = null,
            @SerialName("priceAtPurchase")
            val priceAtPurchase: Int? = null,
            @SerialName("productId")
            val productId: String? = null,
            @SerialName("purchaseQuantity")
            val purchaseQuantity: Int? = null,
            @SerialName("updatedAt")
            val updatedAt: String? = null
        )
    }
}

fun ResponsePendingOrderDTO.Data.toPendingOrderModel(): PendingOrderModel {
    return PendingOrderModel(
        id = id ?: "",
        customerId = customerId ?: "",
        salesmanId = salesmanId ?: "",
        invoiceNumber = invoiceNumber ?: "",
        shipmentDate = expectedShipDate ?: "",
        paymentStatus = paymentStatus ?: "",
        shipmentAddress = shippingAddress ?: "",
        totalPrice = totalPrice.toString(),
        due = paymentDue.toString()
    )
}

fun List<ResponsePendingOrderDTO.Data?>?.toPendingOrderModelList(): List<PendingOrderModel> {
    return this?.filterNotNull()?.map { it.toPendingOrderModel() } ?: emptyList()
}