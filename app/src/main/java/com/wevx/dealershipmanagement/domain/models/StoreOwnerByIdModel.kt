package com.wevx.dealershipmanagement.domain.models

data class StoreOwnerByIdModel(
    val id: String,
    val userId: String,
    val storeOwnerName: String,
    val phone: String,
    val storeOwnerAvatar: String,
    val storeName: String,
    val coordinates: Coordinates,
    val areaNo: Int,
    val address: String
)
