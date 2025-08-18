package com.wevx.dealershipmanagement.presentation.auth.changeProfileImage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.auth_usecase.ChangeProfileImageUseCase
import com.wevx.dealershipmanagement.domain.use_case.storeOwner.CreateStoreOwnerUseCase
import com.wevx.dealershipmanagement.presentation.createStoreOwner.CreateStoreDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChangeProfileImageViewModel @Inject constructor(
    private val changeProfileImageUseCase: ChangeProfileImageUseCase
) : ViewModel() {
    private val _changeProfileImageState = MutableStateFlow(ChangeProfileImageDataState())
    val changeProfileImageState: StateFlow<ChangeProfileImageDataState> = _changeProfileImageState

    fun changeProfileImage(
        avatarFile: File,
        token: String
    ) {
        viewModelScope.launch {
            _changeProfileImageState.value = ChangeProfileImageDataState(loading = true)
            changeProfileImageUseCase.invoke(
                avatarFile,
                token
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _changeProfileImageState.value =
                            ChangeProfileImageDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _changeProfileImageState.value = ChangeProfileImageDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _changeProfileImageState.value =
                            ChangeProfileImageDataState(error = response.message)
                    }
                }

            }
        }
    }
}