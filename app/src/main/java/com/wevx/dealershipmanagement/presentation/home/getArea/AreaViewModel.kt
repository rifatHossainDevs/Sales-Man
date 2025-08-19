package com.wevx.dealershipmanagement.presentation.home.getArea

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.home_usecase.GetAreaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AreaViewModel @Inject constructor(
    private val areaUseCase: GetAreaUseCase
) : ViewModel() {

    private val _areaState = MutableStateFlow(AreaDataState())
    val areaState: StateFlow<AreaDataState> = _areaState


    fun getArea(subDisId: Int) {
        viewModelScope.launch {
            _areaState.value = AreaDataState(loading = true)
            areaUseCase.invoke(subDisId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _areaState.value = AreaDataState(data = response.data)
                        Log.d("TAG", "area: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _areaState.value = AreaDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _areaState.value = AreaDataState(error = response.message)
                        Log.d("TAG", "area error: ${response.message}")
                    }
                }

            }
        }
    }


}