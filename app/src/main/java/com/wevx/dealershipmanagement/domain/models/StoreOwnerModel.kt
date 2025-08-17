package com.wevx.dealershipmanagement.domain.models

data class StoreOwnerModel(
    val id: String,
    val userId: String,
    val storeOwnerName: String,
    val phone: String,
    val storeOwnerAvatar: String,
    val storeName: String,
    val storeImg: String,
    val coordinates: List<String?>? = null,
    val areaNo: Int,
    val address: String,
    val distance: Double? = null
)
