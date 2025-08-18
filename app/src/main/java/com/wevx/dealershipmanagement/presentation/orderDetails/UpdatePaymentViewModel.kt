package com.wevx.dealershipmanagement.presentation.orderDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.RequestUpdatePayment
import com.wevx.dealershipmanagement.domain.use_case.order.GetPaymentByOrderIdUseCase
import com.wevx.dealershipmanagement.domain.use_case.order.UpdatePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePaymentViewModel @Inject constructor(
    private val updatePaymentUseCase: UpdatePaymentUseCase
): ViewModel() {

    private val _updatePaymentState = MutableStateFlow(UpdatePaymentDataState())
    val updatePaymentState: StateFlow<UpdatePaymentDataState> = _updatePaymentState


    fun updatePayment(id: String, requestUpdatePayment: RequestUpdatePayment) {
        viewModelScope.launch {
            _updatePaymentState.value = UpdatePaymentDataState(loading = true)
            updatePaymentUseCase.invoke(id = id, requestUpdatePayment = requestUpdatePayment).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _updatePaymentState.value = UpdatePaymentDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _updatePaymentState.value = UpdatePaymentDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _updatePaymentState.value = UpdatePaymentDataState(error = response.message)
                    }
                }

            }
        }
    }
}