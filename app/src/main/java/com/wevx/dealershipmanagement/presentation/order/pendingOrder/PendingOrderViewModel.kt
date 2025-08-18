package com.wevx.dealershipmanagement.presentation.order.pendingOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.order.GetPendingOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingOrderViewModel @Inject constructor(
    private val pendingOrderUseCase: GetPendingOrderUseCase
) : ViewModel() {

    private val _pendingOrderState = MutableStateFlow(PendingOrderDataState())
    val pendingOrderState: StateFlow<PendingOrderDataState> = _pendingOrderState


    fun getPendingAndCompleteOder(customerId: String, paymentStatus: String) {
        viewModelScope.launch {
            _pendingOrderState.value = PendingOrderDataState(loading = true)
            pendingOrderUseCase.invoke(customerId, paymentStatus).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _pendingOrderState.value = PendingOrderDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _pendingOrderState.value = PendingOrderDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _pendingOrderState.value = PendingOrderDataState(error = response.message)
                    }
                }

            }
        }
    }


}