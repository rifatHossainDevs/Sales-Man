package com.wevx.dealershipmanagement.presentation.auth.logout

import com.wevx.dealershipmanagement.data.dto.logoutDTO.ResponseLogoutDto

data class LogoutDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseLogoutDto? = null
)
