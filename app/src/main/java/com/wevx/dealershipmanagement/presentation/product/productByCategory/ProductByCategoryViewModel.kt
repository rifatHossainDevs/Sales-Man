package com.wevx.dealershipmanagement.presentation.product.productByCategory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.product_usecase.GetProductByCategoryUseCase
import com.wevx.dealershipmanagement.presentation.product.productByCategory.ProductByCategroyDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductByCategoryViewModel @Inject constructor(
    private val productByCategoryUseCase: GetProductByCategoryUseCase
) : ViewModel() {

    private val _productByCategoryState = MutableStateFlow(ProductByCategroyDataState())
    val productByCategoryState: StateFlow<ProductByCategroyDataState> = _productByCategoryState


    fun getProductByCategory(categoryId: String) {
        viewModelScope.launch {
            _productByCategoryState.value = ProductByCategroyDataState(loading = true)
            productByCategoryUseCase.invoke(categoryId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _productByCategoryState.value =
                            ProductByCategroyDataState(data = response.data)
                        Log.d("productByCategory", "productByCategory: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _productByCategoryState.value = ProductByCategroyDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _productByCategoryState.value =
                            ProductByCategroyDataState(error = response.message)
                        Log.d("TAG", "productByCategory error: ${response.message}")
                    }
                }

            }
        }
    }


}