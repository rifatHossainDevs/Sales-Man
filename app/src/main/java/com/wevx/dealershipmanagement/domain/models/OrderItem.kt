package com.wevx.dealershipmanagement.domain.models

data class OrderItem(
    val productId: String,
    val purchaseQuantity: Int,
    val priceAtPurchase: Double
)
