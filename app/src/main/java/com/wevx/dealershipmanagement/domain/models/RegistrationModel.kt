package com.wevx.dealershipmanagement.domain.models

data class RegistrationModel(
    val userId: String,
    val userEmail: String,
    val userName: String,
    val nid: String,
    val userPhoneNumber: String,
    val userProfileImageUrl: String? = null,
    val userRole: String
)
