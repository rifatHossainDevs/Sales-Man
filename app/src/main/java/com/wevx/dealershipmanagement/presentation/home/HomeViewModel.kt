package com.wevx.dealershipmanagement.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wevx.dealershipmanagement.core.common.Resource
import com.wevx.dealershipmanagement.domain.models.StoreOwnerModel
import com.wevx.dealershipmanagement.domain.use_case.storeOwner.GetStoreOwnerByAreaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStoreOwnerByAreaUseCase: GetStoreOwnerByAreaUseCase
) : ViewModel() {

    // Spinner positions
    var selectedDivisionIndex: Int = 0
    var selectedDivisionId: Int = 0

    var selectedDistrictIndex: Int = 0
    var selectedDistrictId: Int = 0

    var selectedSubDistrictIndex: Int = 0
    var selectedSubDistrictId: Int = 0

    var selectedAreaIndex: Int = 0
    var selectedAreaId: Int = 0
    val isLoading = MutableLiveData<Boolean>()
    val storeOwners = MutableLiveData<List<StoreOwnerModel>>()
    val errorMessage = MutableLiveData<String>()

    fun fetchStoreOwners(areaId: Int) {
        viewModelScope.launch {
            getStoreOwnerByAreaUseCase(areaId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> isLoading.value = true
                    is Resource.Success -> {
                        isLoading.value = false
                        storeOwners.value = resource.data ?: emptyList()
                    }

                    is Resource.Error -> {
                        isLoading.value = false
                        errorMessage.value = resource.message ?: "Unknown error"
                        storeOwners.value = emptyList()
                    }
                }
            }
        }
    }
}
