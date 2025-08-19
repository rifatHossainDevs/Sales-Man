package com.wevx.dealershipmanagement.presentation.shipment.getShipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.order.GetShipmentByOrderIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetShipmentViewModel @Inject constructor(
    private val getShipmentByOrderIdUseCase: GetShipmentByOrderIdUseCase
): ViewModel() {

    private val _getShipmentState = MutableStateFlow(GetShipmentDataState())
    val getShipmentState: StateFlow<GetShipmentDataState> = _getShipmentState


    fun getShipmentByOrderId(orderId: String) {
        viewModelScope.launch {
            _getShipmentState.value = GetShipmentDataState(loading = true)
            getShipmentByOrderIdUseCase.invoke(orderId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _getShipmentState.value = GetShipmentDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _getShipmentState.value = GetShipmentDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _getShipmentState.value = GetShipmentDataState(error = response.message)
                    }
                }

            }
        }
    }
}