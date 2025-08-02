package com.wevx.dealershipmanagement.presentation.auth.changePassword

import com.wevx.dealershipmanagement.data.dto.changePasswordDTO.ResponseChangePasswordDTO
import com.wevx.dealershipmanagement.domain.models.LoginModel
import com.wevx.dealershipmanagement.domain.models.ProfileModel

data class ChangePasswordDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ResponseChangePasswordDTO? = null
)
