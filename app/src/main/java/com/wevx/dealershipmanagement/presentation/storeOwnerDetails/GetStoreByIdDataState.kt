package com.wevx.dealershipmanagement.presentation.storeOwnerDetails

import com.wevx.dealershipmanagement.domain.models.StoreOwnerByIdModel

data class GetStoreByIdDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: StoreOwnerByIdModel ?= null
)
