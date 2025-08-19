package com.wevx.dealershipmanagement.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.authDto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginDataState())
    val loginState: StateFlow<LoginDataState> = _loginState

    fun loginUser(requestLogin: RequestLogin) {
        viewModelScope.launch {
            _loginState.value = LoginDataState(loading = true)
            loginUseCase.invoke(requestLogin).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _loginState.value = LoginDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _loginState.value = LoginDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _loginState.value = LoginDataState(error = response.message)
                    }
                }

            }
        }
    }
}