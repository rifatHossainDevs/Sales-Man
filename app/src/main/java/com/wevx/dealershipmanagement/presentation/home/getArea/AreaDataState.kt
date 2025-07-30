package com.wevx.dealershipmanagement.presentation.home.getArea

import com.wevx.dealershipmanagement.domain.models.AreaModel

data class AreaDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<AreaModel>? = null
)
