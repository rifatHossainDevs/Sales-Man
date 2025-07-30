package com.wevx.dealershipmanagement.presentation.home.getDistrict

import com.wevx.dealershipmanagement.domain.models.DistrictModel

data class DistrictDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<DistrictModel>? = null
)
