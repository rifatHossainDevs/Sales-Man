package com.wevx.dealershipmanagement.presentation.order.createPayment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.data.dto.paymentDto.RequestPaymentDTO
import com.wevx.dealershipmanagement.domain.use_case.order.CreatePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePaymentViewModel @Inject constructor(
    private val createPaymentUseCase: CreatePaymentUseCase
) : ViewModel() {

    private val _createPaymentState = MutableStateFlow(CreatePaymentDataState())
    val createPaymentState: StateFlow<CreatePaymentDataState> = _createPaymentState


    fun createPayment(requestPaymentDTO: RequestPaymentDTO, token: String) {
        viewModelScope.launch {
            _createPaymentState.value = CreatePaymentDataState(loading = true)
            createPaymentUseCase.invoke(requestPaymentDTO, token).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _createPaymentState.value = CreatePaymentDataState(data = response.data)
                        Log.d("TAG", "create order success: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _createPaymentState.value = CreatePaymentDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _createPaymentState.value = CreatePaymentDataState(error = response.message)
                        Log.d("TAG", "create order error: ${response.message}")
                    }
                }

            }
        }
    }


}