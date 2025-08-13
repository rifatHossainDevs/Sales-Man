package com.wevx.dealershipmanagement.presentation.auth.refreshToken

import com.wevx.dealershipmanagement.data.dto.refreshTokenDto.ResponseRefreshTokenDTO

data class RefreshTokenDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseRefreshTokenDTO? = null
)