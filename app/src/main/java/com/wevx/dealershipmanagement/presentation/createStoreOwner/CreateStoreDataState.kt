package com.wevx.dealershipmanagement.presentation.createStoreOwner

import com.wevx.dealershipmanagement.domain.models.CreateStoreModel

data class CreateStoreDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: CreateStoreModel ?= null
)
