package com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByArea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.storeOwner.GetStoreOwnerByAreaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreOwnerViewModel @Inject constructor(
    private val storeOwnerUseCase: GetStoreOwnerByAreaUseCase
) : ViewModel() {

    private val _storeOwnerState = MutableStateFlow(StoreOwnerDataState())
    val storeOwnerState: StateFlow<StoreOwnerDataState> = _storeOwnerState

    fun getStoreOwnerByArea(areaId: Int) {
        viewModelScope.launch {
            _storeOwnerState.value = StoreOwnerDataState(loading = true)
            storeOwnerUseCase.invoke(areaId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _storeOwnerState.value = StoreOwnerDataState(data = response.data)
                    }
                    is Resource.Loading -> {
                        _storeOwnerState.value = StoreOwnerDataState(loading = true)
                    }
                    is Resource.Error -> {
                        _storeOwnerState.value = StoreOwnerDataState(error = response.message)
                    }
                }

            }
        }
    }


}