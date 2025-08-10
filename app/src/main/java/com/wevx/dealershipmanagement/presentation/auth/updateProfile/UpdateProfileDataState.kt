package com.wevx.dealershipmanagement.presentation.auth.updateProfile

import com.wevx.dealershipmanagement.domain.models.UpdateProfileModel

data class UpdateProfileDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: UpdateProfileModel? = null
)
