package com.wevx.dealershipmanagement.presentation.auth.changePassword

import com.wevx.dealershipmanagement.data.dto.authDto.changePasswordDTO.ResponseChangePasswordDTO

data class ChangePasswordDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseChangePasswordDTO? = null
)
