package com.wevx.dealershipmanagement.presentation.orderDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.RequestUpdateOrder
import com.wevx.dealershipmanagement.domain.use_case.order.UpdateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateOrderViewModel @Inject constructor(
    private val updateOrderUseCase: UpdateOrderUseCase
): ViewModel() {

    private val _updateOrderState = MutableStateFlow(UpdateOrderDataState())
    val updateOrderState: StateFlow<UpdateOrderDataState> = _updateOrderState


    fun updateOrder(
        id: String,
        requestUpdateOrder: RequestUpdateOrder
    ) {
        viewModelScope.launch {
            _updateOrderState.value = UpdateOrderDataState(loading = true)
            updateOrderUseCase.invoke(
                id = id,
                requestUpdateOrder = requestUpdateOrder
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _updateOrderState.value = UpdateOrderDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _updateOrderState.value = UpdateOrderDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _updateOrderState.value = UpdateOrderDataState(error = response.message)
                        Log.d("TAG", "updateOrder: ${response.message}")
                    }
                }

            }
        }
    }
}