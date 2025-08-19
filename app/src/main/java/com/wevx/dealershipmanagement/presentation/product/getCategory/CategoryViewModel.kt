package com.wevx.dealershipmanagement.presentation.product.getCategory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.use_case.product_usecase.GetCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCase: GetCategoryUseCase
) : ViewModel() {

    private val _categoryState = MutableStateFlow(CategoryDataState())
    val categoryState: StateFlow<CategoryDataState> = _categoryState

    fun getCategory() {
        viewModelScope.launch {
            _categoryState.value = CategoryDataState(loading = true)
            categoryUseCase.invoke().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _categoryState.value = CategoryDataState(data = response.data)
                        Log.d("TAG", "category: ${response.data}")
                    }

                    is Resource.Loading -> {
                        _categoryState.value = CategoryDataState(loading = true)
                    }

                    is Resource.Error -> {
                        _categoryState.value = CategoryDataState(error = response.message)
                        Log.d("TAG", "category error: ${response.message}")
                    }
                }

            }
        }
    }


}