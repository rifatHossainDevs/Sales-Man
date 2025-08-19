package com.wevx.dealershipmanagement.presentation.home.getSubDistrict

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.home_usecase.GetSubDistrictUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubDistrictViewModel @Inject constructor(
    private val subDistrictUseCase: GetSubDistrictUseCase
) : ViewModel() {

    private val _subDistrictState = MutableStateFlow(SubDistrictDataState())
    val subDistrictState: StateFlow<SubDistrictDataState> = _subDistrictState


    fun getSubDistrict(disId: Int) {
        viewModelScope.launch {
            _subDistrictState.value = SubDistrictDataState(loading = true)
            subDistrictUseCase.invoke(disId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _subDistrictState.value = SubDistrictDataState(data = response.data)
                        Log.d("TAG", "subDistrict: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _subDistrictState.value = SubDistrictDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _subDistrictState.value = SubDistrictDataState(error = response.message)
                        Log.d("TAG", "sub District error: ${response.message}")
                    }
                }

            }
        }
    }


}