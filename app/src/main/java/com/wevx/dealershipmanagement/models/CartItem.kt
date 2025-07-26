package com.wevx.dealershipmanagement.models

data class CartItem(val product: Products, var purchaseQuantity: Double){
    val subtotal: Double
        get() = purchaseQuantity * product.productPricePerUnit
}
