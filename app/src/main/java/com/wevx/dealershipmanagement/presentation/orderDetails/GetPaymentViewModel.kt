package com.wevx.dealershipmanagement.presentation.orderDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.order.GetPaymentByOrderIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetPaymentViewModel @Inject constructor(
    private val getPaymentByOrderIdUseCase: GetPaymentByOrderIdUseCase
): ViewModel() {

    private val _getPaymentState = MutableStateFlow(GetPaymentDataState())
    val getPaymentState: StateFlow<GetPaymentDataState> = _getPaymentState


    fun getPaymentByOrderId(orderId: String) {
        viewModelScope.launch {
            _getPaymentState.value = GetPaymentDataState(loading = true)
            getPaymentByOrderIdUseCase.invoke(orderId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _getPaymentState.value = GetPaymentDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _getPaymentState.value = GetPaymentDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _getPaymentState.value = GetPaymentDataState(error = response.message)
                    }
                }

            }
        }
    }
}