package com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByArea

import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel

data class StoreOwnerDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<StoreOwnerModel>? = null
)
