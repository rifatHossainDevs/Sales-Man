package com.wevx.dealershipmanagement.domain.models

data class Products(
    val productId: String,
    val productName: String,
    val productPricePerUnit: Double,
    val productQty: Double,
    val productUnit: String,
    val productCategory: String,
    val brandName: String,
    val imageUrl: String
) {
    val subtotal: Double get() = productQty * productPricePerUnit
}
