package com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByDistrict

import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel

data class StoreOwnerByDisDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<StoreOwnerModel>? = null
)
