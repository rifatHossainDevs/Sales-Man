package com.wevx.dealershipmanagement.presentation.orderDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestUpdateShipment
import com.wevx.dealershipmanagement.domain.use_case.order.GetShipmentByOrderIdUseCase
import com.wevx.dealershipmanagement.domain.use_case.order.UpdateShipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateShipmentViewModel  @Inject constructor(
    private val updateShipmentUseCase: UpdateShipmentUseCase
): ViewModel() {

    private val _updateShipmentState = MutableStateFlow(UpdateShipmentDataState())
    val updateShipmentState: StateFlow<UpdateShipmentDataState> = _updateShipmentState


    fun updateShipment(
        id: String,
        requestUpdateShipment: RequestUpdateShipment
    ) {
        viewModelScope.launch {
            _updateShipmentState.value = UpdateShipmentDataState(loading = true)
            updateShipmentUseCase.invoke(
                id = id,
                requestUpdateShipment = requestUpdateShipment
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _updateShipmentState.value = UpdateShipmentDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _updateShipmentState.value = UpdateShipmentDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _updateShipmentState.value = UpdateShipmentDataState(error = response.message)
                    }
                }

            }
        }
    }
}