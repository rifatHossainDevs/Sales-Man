package com.wevx.dealershipmanagement.presentation.order.pendingOrder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.order.GetPendingOrderUseCase
import com.wevx.dealershipmanagement.presentation.product.getAllProduct.ProductDataState
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


    fun getAllProduct(customerId: String) {
        viewModelScope.launch {
            _pendingOrderState.value = PendingOrderDataState(loading = true)
            pendingOrderUseCase.invoke(customerId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _pendingOrderState.value = PendingOrderDataState(data = response.data)
                        Log.d("TAG", "pending order success: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _pendingOrderState.value = PendingOrderDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _pendingOrderState.value = PendingOrderDataState(error = response.message)
                        Log.d("TAG", "pending order error: ${response.message}")
                    }
                }

            }
        }
    }


}