package com.wevx.dealershipmanagement.presentation.auth.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.registrationDto.RequestRegistration
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel() {
    private val _registrationState = MutableStateFlow(RegistrationDataState())
    val registrationState: StateFlow<RegistrationDataState> = _registrationState

    fun registrationUser(requestRegistration: RequestRegistration) {
        viewModelScope.launch {
            _registrationState.value = RegistrationDataState(loading = true)
            registrationUseCase.invoke(requestRegistration).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _registrationState.value = RegistrationDataState(data = response.data)
                        Log.d("registration", "registrationUser: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _registrationState.value = RegistrationDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _registrationState.value = RegistrationDataState(error = response.message)
                    }
                }

            }
        }
    }
}