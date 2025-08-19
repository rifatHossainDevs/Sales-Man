package com.wevx.dealershipmanagement.presentation.auth.refreshToken

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.authDto.RequestRefreshToken
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.RefreshTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefreshTokenViewModel @Inject constructor(
    private val refreshTokenUseCase: RefreshTokenUseCase,
) : ViewModel() {
    private val _refreshTokenState = MutableStateFlow(RefreshTokenDataState())
    val refreshTokenState: StateFlow<RefreshTokenDataState> = _refreshTokenState

    fun getRefreshAccessToken(request: RequestRefreshToken) {
        viewModelScope.launch {
            refreshTokenUseCase.invoke(request).collect { response->
                when (response) {
                    is Resource.Success -> {
                        _refreshTokenState.value = RefreshTokenDataState(data = response.data)
                    }
                    is Resource.Error -> {
                        _refreshTokenState.value = RefreshTokenDataState(error = response.message)
                    }
                    is Resource.Loading -> {
                        _refreshTokenState.value = RefreshTokenDataState(loading = true)
                    }
                }
            }
        }
    }
}