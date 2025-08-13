package com.wevx.dealershipmanagement.presentation.order.createShipment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.shipmentDto.RequestShipmentDTO
import com.wevx.dealershipmanagement.domain.use_case.order.CreateShipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateShipmentViewModel @Inject constructor(
    private val createShipmentUseCase: CreateShipmentUseCase
) : ViewModel() {

    private val _createShipmentState = MutableStateFlow(CreateShipmentDataState())
    val createShipmentState: StateFlow<CreateShipmentDataState> = _createShipmentState


    fun createShipment(requestShipmentDTO: RequestShipmentDTO, token: String) {
        viewModelScope.launch {
            _createShipmentState.value = CreateShipmentDataState(loading = true)
            createShipmentUseCase.invoke(requestShipmentDTO, token).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _createShipmentState.value = CreateShipmentDataState(data = response.data)
                        Log.d("TAG", "create shipment success: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _createShipmentState.value = CreateShipmentDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _createShipmentState.value = CreateShipmentDataState(error = response.message)
                        Log.d("TAG", "create shipment error: ${response.message}")
                    }
                }

            }
        }
    }


}