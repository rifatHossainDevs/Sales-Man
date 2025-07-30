package com.wevx.dealershipmanagement.domain.models

data class ProductModel(
    val productId: String,
    val productName: String,
    val brandName: String,
    val description: String,
    val price: Double,
    val unit: String,
    val offerPrice: Double,
    val stockQuantity: Double,
    val categoryId: String,
    val imageUrl: String,
    val isActive: Boolean
)
