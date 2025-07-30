package com.wevx.dealershipmanagement.presentation.product.getCategory

import com.wevx.dealershipmanagement.domain.models.CategoryModel

data class CategoryDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<CategoryModel>? = null
)
