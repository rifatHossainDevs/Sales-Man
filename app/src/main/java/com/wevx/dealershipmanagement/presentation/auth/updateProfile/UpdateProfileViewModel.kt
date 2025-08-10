package com.wevx.dealershipmanagement.presentation.auth.updateProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.updateProfileDto.RequestUpdateProfile
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.GetProfileUseCase
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.UpdateProfileUseCase
import com.wevx.dealershipmanagement.presentation.auth.profile.GetProfileDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private val _updateProfileState = MutableStateFlow(UpdateProfileDataState())
    val updateProfileState: StateFlow<UpdateProfileDataState> = _updateProfileState

    fun updateProfile(requestUpdateProfile: RequestUpdateProfile, token: String) {
        viewModelScope.launch {
            _updateProfileState.value = UpdateProfileDataState(loading = true)
            updateProfileUseCase.invoke(requestUpdateProfile, token).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _updateProfileState.value = UpdateProfileDataState(data = response.data)
                        Log.d("profileData", "getProfile: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _updateProfileState.value = UpdateProfileDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _updateProfileState.value = UpdateProfileDataState(error = response.message)
                    }
                }

            }
        }
    }
}