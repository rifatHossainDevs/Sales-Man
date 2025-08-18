package com.wevx.dealershipmanagement.presentation.order.completeOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.order.GetCompleteOrderUseCase
import com.wevx.dealershipmanagement.presentation.order.pendingOrder.PendingOrderDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteOrderViewModel @Inject constructor(
    private val completeOrderUseCase: GetCompleteOrderUseCase
) : ViewModel() {

    private val _completeOrderState = MutableStateFlow(CompleteOrderDataState())
    val completeOrderState: StateFlow<CompleteOrderDataState> = _completeOrderState


    fun getPendingAndCompleteOder(customerId: String, paymentStatus: String) {
        viewModelScope.launch {
            _completeOrderState.value = CompleteOrderDataState(loading = true)
            completeOrderUseCase.invoke(customerId, paymentStatus).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _completeOrderState.value = CompleteOrderDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _completeOrderState.value = CompleteOrderDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _completeOrderState.value = CompleteOrderDataState(error = response.message)
                    }
                }

            }
        }
    }


}