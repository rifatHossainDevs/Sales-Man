package com.wevx.dealershipmanagement.domain.models

data class CartItem(val product: Products, var purchaseQuantity: Double){
    val subtotal: Double
        get() = purchaseQuantity * product.productPricePerUnit
}
