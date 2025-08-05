package com.wevx.dealershipmanagement.domain.models

data class LoginModel(
    val userId: String,
    val userEmail: String,
    val userName: String,
    val userPhoneNumber: String,
    val nidNumber: String? = null,
    val userProfileImageUrl: String? = null,
    val userType: String,
    val companyId: String? = null,
    val isActive: Boolean,
    val refreshToken: String? = null,
    val accessToken: String? = null
)
