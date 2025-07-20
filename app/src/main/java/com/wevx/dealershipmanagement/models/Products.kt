package com.wevx.dealershipmanagement.models

data class Products(
    val productId: String,
    val productName: String,
    val productPricePerUnit: Double,
    val productQty: Double,
    val productUnit: String,
    val productCategory: String
) {
    val subtotal: Double get() = productQty * productQty
}
