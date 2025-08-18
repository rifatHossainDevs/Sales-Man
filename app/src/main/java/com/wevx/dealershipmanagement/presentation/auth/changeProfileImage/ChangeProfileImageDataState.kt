package com.wevx.dealershipmanagement.presentation.auth.changeProfileImage

import com.wevx.dealershipmanagement.data.dto.ResponseChangeProfileImage

data class ChangeProfileImageDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseChangeProfileImage ?= null
)
