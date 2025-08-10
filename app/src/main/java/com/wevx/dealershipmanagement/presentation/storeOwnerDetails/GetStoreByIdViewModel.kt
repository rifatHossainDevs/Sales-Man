package com.wevx.dealershipmanagement.presentation.storeOwnerDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.storeOwner.GetStoreOwnerByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetStoreByIdViewModel @Inject constructor(
    private val getStoreByIdUseCase: GetStoreOwnerByIdUseCase
) : ViewModel() {
    private val _getStoreByIdState = MutableStateFlow(GetStoreByIdDataState())
    val getStoreByIdState: StateFlow<GetStoreByIdDataState> = _getStoreByIdState

    fun getStoreById(id: String) {
        viewModelScope.launch {
            _getStoreByIdState.value = GetStoreByIdDataState(loading = true)
            getStoreByIdUseCase.invoke(id).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _getStoreByIdState.value = GetStoreByIdDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _getStoreByIdState.value = GetStoreByIdDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _getStoreByIdState.value = GetStoreByIdDataState(error = response.message)
                    }
                }

            }
        }
    }
}