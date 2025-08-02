package com.wevx.dealershipmanagement.presentation.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.loginDto.RequestLogin
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: GetProfileUseCase
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileDataState())
    val profileState: StateFlow<ProfileDataState> = _profileState

    fun getProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileDataState(loading = true)
            profileUseCase.invoke().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _profileState.value = ProfileDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _profileState.value = ProfileDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _profileState.value = ProfileDataState(error = response.message)
                    }
                }

            }
        }
    }
}