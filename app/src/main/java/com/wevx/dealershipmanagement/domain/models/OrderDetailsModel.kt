package com.wevx.dealershipmanagement.domain.models

data class OrderDetailsModel(val name: String, val quantity: Double, val unit: String, val price: Double, val total: Double){
    val subtotal: Double
        get() = (quantity.times(price)).toDouble()
}
