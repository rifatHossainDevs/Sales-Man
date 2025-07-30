package com.wevx.dealershipmanagement.presentation.product.getAllProduct

import com.wevx.dealershipmanagement.domain.models.ProductModel

data class ProductDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<ProductModel>? = null
)
