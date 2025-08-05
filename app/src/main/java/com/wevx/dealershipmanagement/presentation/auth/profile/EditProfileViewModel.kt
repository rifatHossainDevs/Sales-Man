package com.wevx.dealershipmanagement.presentation.auth.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileUseCase: GetProfileUseCase
) : ViewModel() {
    private val _profileState = MutableStateFlow(EditProfileDataState())
    val profileState: StateFlow<EditProfileDataState> = _profileState

    fun getProfile() {
        viewModelScope.launch {
            _profileState.value = EditProfileDataState(loading = true)
            profileUseCase.invoke().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _profileState.value = EditProfileDataState(data = response.data)
                        Log.d("profileData", "getProfile: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _profileState.value = EditProfileDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _profileState.value = EditProfileDataState(error = response.message)
                    }
                }

            }
        }
    }
}