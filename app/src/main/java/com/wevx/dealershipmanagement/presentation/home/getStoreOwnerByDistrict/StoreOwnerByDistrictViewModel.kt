package com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByDistrict

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.storeOwner.GetStoreOwnerByAreaUseCase
import com.wevx.dealershipmanagement.domain.use_case.storeOwner.GetStoreOwnerByDistrictUseCase
import com.wevx.dealershipmanagement.presentation.home.getStoreOwnerByArea.StoreOwnerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreOwnerByDistrictViewModel @Inject constructor(
    private val storeOwnerByDistrictUseCase: GetStoreOwnerByDistrictUseCase
) : ViewModel() {

    private val _storeOwnerByDisState = MutableStateFlow(StoreOwnerByDisDataState())
    val storeOwnerByDisState: StateFlow<StoreOwnerByDisDataState> = _storeOwnerByDisState


    fun getStoreOwnerByDis(disId: Int) {
        viewModelScope.launch {
            _storeOwnerByDisState.value = StoreOwnerByDisDataState(loading = true)
            storeOwnerByDistrictUseCase.invoke(disId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _storeOwnerByDisState.value = StoreOwnerByDisDataState(data = response.data)
                        Log.d("storeOwner", "Store Owner: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _storeOwnerByDisState.value = StoreOwnerByDisDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _storeOwnerByDisState.value = StoreOwnerByDisDataState(error = response.message)
                        Log.d("TAG", "error: ${response.message}")
                    }
                }

            }
        }
    }


}