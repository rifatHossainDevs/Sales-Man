package com.wevx.dealershipmanagement.presentation.product.getProductById

import com.wevx.dealershipmanagement.data.dto.productById.ResponseProductByIdDTO

data class ProductByIdDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseProductByIdDTO? = null
)
