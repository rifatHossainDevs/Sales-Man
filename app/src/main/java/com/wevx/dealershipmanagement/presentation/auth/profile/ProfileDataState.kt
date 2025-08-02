package com.wevx.dealershipmanagement.presentation.auth.profile

import com.wevx.dealershipmanagement.domain.models.LoginModel
import com.wevx.dealershipmanagement.domain.models.ProfileModel

data class ProfileDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ProfileModel? = null
)
