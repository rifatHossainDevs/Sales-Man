package com.wevx.dealershipmanagement.presentation.order.createOrder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.createOrderDto.RequestCreateOrderDTO
import com.wevx.dealershipmanagement.domain.use_case.order.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _createOrderState = MutableStateFlow(CreateOrderDataState())
    val createOrderState: StateFlow<CreateOrderDataState> = _createOrderState


    fun createOrder(requestCreateOrderDTO: RequestCreateOrderDTO, token: String) {
        viewModelScope.launch {
            _createOrderState.value = CreateOrderDataState(loading = true)
            createOrderUseCase.invoke(requestCreateOrderDTO, token).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _createOrderState.value = CreateOrderDataState(data = response.data)
                        Log.d("TAG", "create order success: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _createOrderState.value = CreateOrderDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _createOrderState.value = CreateOrderDataState(error = response.message)
                        Log.d("TAG", "create order error: ${response.message}")
                    }
                }

            }
        }
    }


}