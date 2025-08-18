package com.wevx.dealershipmanagement.presentation.orderDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.order.GetOrderByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : ViewModel() {

    private val _orderDetailsState = MutableStateFlow(OrderDetailsDataState())
    val orderDetailsState: StateFlow<OrderDetailsDataState> = _orderDetailsState


    fun getOderDetails(orderId: String) {
        viewModelScope.launch {
            _orderDetailsState.value = OrderDetailsDataState(loading = true)
            getOrderByIdUseCase.invoke(orderId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _orderDetailsState.value = OrderDetailsDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _orderDetailsState.value = OrderDetailsDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _orderDetailsState.value = OrderDetailsDataState(error = response.message)
                    }
                }

            }
        }
    }


}