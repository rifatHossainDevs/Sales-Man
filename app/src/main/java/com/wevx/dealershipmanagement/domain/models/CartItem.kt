package com.wevx.dealershipmanagement.domain.models

data class CartItem(val product: ProductModel, var purchaseQuantity: Double){
    val subtotal: Double
        get() = purchaseQuantity * product.price
}
