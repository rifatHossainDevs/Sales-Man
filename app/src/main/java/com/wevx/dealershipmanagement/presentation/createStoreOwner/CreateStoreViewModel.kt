package com.wevx.dealershipmanagement.presentation.createStoreOwner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.storeOwner.CreateStoreOwnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateStoreViewModel @Inject constructor(
    private val createStoreUseCase: CreateStoreOwnerUseCase
) : ViewModel() {
    private val _createStoreState = MutableStateFlow(CreateStoreDataState())
    val createStoreState: StateFlow<CreateStoreDataState> = _createStoreState

    fun createStore(userId: String,
                     storeName: String,
                     storePictureFile: File,
                     coordinate1: String,
                     coordinate2: String,
                     areaNo: String,
                     address: String,
                     storeOwnerName: String,
                     phone: String,
                     avatarFile: File,
                     subDisNo: String,
                    token: String) {
        viewModelScope.launch {
            _createStoreState.value = CreateStoreDataState(loading = true)
            createStoreUseCase.invoke(
                userId,
                storeName,
                storePictureFile,
                coordinate1,
                coordinate2,
                areaNo,
                address,
                storeOwnerName,
                phone,
                avatarFile,
                subDisNo,
                token
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _createStoreState.value = CreateStoreDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _createStoreState.value = CreateStoreDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _createStoreState.value = CreateStoreDataState(error = response.message)
                    }
                }

            }
        }
    }
}