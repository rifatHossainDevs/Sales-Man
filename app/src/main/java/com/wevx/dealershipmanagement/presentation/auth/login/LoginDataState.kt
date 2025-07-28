package com.wevx.dealershipmanagement.presentation.auth.login

import com.wevx.dealershipmanagement.domain.models.LoginModel

data class LoginDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: LoginModel? = null
)
