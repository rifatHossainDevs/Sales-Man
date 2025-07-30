package com.wevx.dealershipmanagement.presentation.home.getSubDistrict

import com.wevx.dealershipmanagement.domain.models.SubDistrictModel

data class SubDistrictDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<SubDistrictModel>? = null
)
