package com.wevx.dealershipmanagement.presentation.auth.registration

import com.wevx.dealershipmanagement.domain.models.RegistrationModel

data class RegistrationDataState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: RegistrationModel? = null
)
