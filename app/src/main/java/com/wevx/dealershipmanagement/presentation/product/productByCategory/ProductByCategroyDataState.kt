package com.wevx.dealershipmanagement.presentation.product.productByCategory

import com.wevx.dealershipmanagement.domain.models.ProductModel

data class ProductByCategroyDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)