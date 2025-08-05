package com.wevx.dealershipmanagement.presentation.auth.profile

import com.wevx.dealershipmanagement.domain.models.ProfileModel

data class EditProfileDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ProfileModel? = null
)
