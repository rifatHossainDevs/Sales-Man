package com.wevx.dealershipmanagement.presentation.auth.logout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.domain.models.DistrictModel
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.LoginUseCase
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.LogoutUseCase
import com.wevx.dealershipmanagement.domain.use_case.home_usecase.GetDistrictUseCase
import com.wevx.dealershipmanagement.presentation.auth.login.LoginDataState
import com.wevx.dealershipmanagement.presentation.home.getDistrict.DistrictDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _logoutState = MutableStateFlow(LogoutDataState())
    val logoutState: StateFlow<LogoutDataState> = _logoutState

    fun logoutUser() {
        viewModelScope.launch {
            _logoutState.value = LogoutDataState(loading = true)
            logoutUseCase.invoke().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _logoutState.value = LogoutDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _logoutState.value = LogoutDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _logoutState.value = LogoutDataState(error = response.message)
                    }
                }

            }
        }
    }
}