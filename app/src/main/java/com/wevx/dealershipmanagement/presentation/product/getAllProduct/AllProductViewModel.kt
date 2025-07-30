package com.wevx.dealershipmanagement.presentation.product.getAllProduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.product_usecase.GetAllProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProductViewModel @Inject constructor(
    private val allProductUseCase: GetAllProductUseCase
) : ViewModel() {

    private val _allProductState = MutableStateFlow(ProductDataState())
    val allProductState: StateFlow<ProductDataState> = _allProductState


    fun getAllProduct() {
        viewModelScope.launch {
            _allProductState.value = ProductDataState(loading = true)
            allProductUseCase.invoke().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _allProductState.value = ProductDataState(data = response.data)
                        Log.d("TAG", "allProduct: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _allProductState.value = ProductDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _allProductState.value = ProductDataState(error = response.message)
                        Log.d("TAG", "category error: ${response.message}")
                    }
                }

            }
        }
    }


}