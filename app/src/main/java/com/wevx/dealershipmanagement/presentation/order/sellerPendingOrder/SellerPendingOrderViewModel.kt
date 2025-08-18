package com.wevx.dealershipmanagement.presentation.order.sellerPendingOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.order.SellerPendingOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerPendingOrderViewModel @Inject constructor(
    private val sellerPendingOrderUseCase: SellerPendingOrderUseCase
) : ViewModel() {

    private val _sellerPendingState = MutableStateFlow(SellerPendingOrderDataState())
    val sellerPendingState: StateFlow<SellerPendingOrderDataState> = _sellerPendingState


    fun sellerPendingOrder(sellerId: String, paymentStatus: String) {
        viewModelScope.launch {
            _sellerPendingState.value = SellerPendingOrderDataState(loading = true)
            sellerPendingOrderUseCase.invoke(sellerId, paymentStatus).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _sellerPendingState.value = SellerPendingOrderDataState(data = response.data)
                    }

                    is Resource.Loading -> {
                        _sellerPendingState.value = SellerPendingOrderDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _sellerPendingState.value = SellerPendingOrderDataState(error = response.message)
                    }
                }

            }
        }
    }


}