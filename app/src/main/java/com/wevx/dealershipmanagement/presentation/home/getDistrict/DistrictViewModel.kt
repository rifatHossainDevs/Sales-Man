package com.wevx.dealershipmanagement.presentation.home.getDistrict

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.home_usecase.GetDistrictUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistrictViewModel @Inject constructor(
    private val districtUseCase: GetDistrictUseCase
) : ViewModel() {

    private val _districtState = MutableStateFlow(DistrictDataState())
    val districtState: StateFlow<DistrictDataState> = _districtState


    fun getDistrict(divId: Int) {
        viewModelScope.launch {
            _districtState.value = DistrictDataState(loading = true)
            districtUseCase.invoke(divId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _districtState.value = DistrictDataState(data = response.data)
                        Log.d("TAG", "district: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _districtState.value = DistrictDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _districtState.value = DistrictDataState(error = response.message)
                        Log.d("TAG", "error: ${response.message}")
                    }
                }

            }
        }
    }


}