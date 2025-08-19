package com.wevx.dealershipmanagement.presentation.auth.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.authDto.changePasswordDTO.RequestChangePasswordDto
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {
    private val _changePasswordState = MutableStateFlow(ChangePasswordDataState())
    val changePasswordState: StateFlow<ChangePasswordDataState> = _changePasswordState

    fun changePassword(requestChangePassword: RequestChangePasswordDto, token: String) {
        viewModelScope.launch {
            _changePasswordState.value = ChangePasswordDataState(loading = true)
            changePasswordUseCase.invoke(requestChangePassword, token).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _changePasswordState.value = ChangePasswordDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _changePasswordState.value = ChangePasswordDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _changePasswordState.value = ChangePasswordDataState(error = response.message)
                    }
                }

            }
        }
    }
}