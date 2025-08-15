package com.wevx.dealershipmanagement.presentation.order.pendingAndCompleteOrder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.pendingAndCompleteOrderDto.RequestPendingAndCompleteOrder
import com.wevx.dealershipmanagement.domain.use_case.order.GetPendingAndCompleteOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingAndCompleteOrderViewModel @Inject constructor(
    private val pendingOrderUseCase: GetPendingAndCompleteOrderUseCase
) : ViewModel() {

    private val _pendingOrderState = MutableStateFlow(PendingAndCompleteOrderDataState())
    val pendingOrderState: StateFlow<PendingAndCompleteOrderDataState> = _pendingOrderState


    fun getPendingAndCompleteOder(customerId: String, requestPendingAndCompleteOrder: RequestPendingAndCompleteOrder) {
        viewModelScope.launch {
            _pendingOrderState.value = PendingAndCompleteOrderDataState(loading = true)
            pendingOrderUseCase.invoke(customerId, requestPendingAndCompleteOrder).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _pendingOrderState.value = PendingAndCompleteOrderDataState(data = response.data)
                        Log.d("TAG", "pending and complete order success: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _pendingOrderState.value = PendingAndCompleteOrderDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _pendingOrderState.value = PendingAndCompleteOrderDataState(error = response.message)
                        Log.d("TAG", "pending and complete order error: ${response.message}")
                    }
                }

            }
        }
    }


}