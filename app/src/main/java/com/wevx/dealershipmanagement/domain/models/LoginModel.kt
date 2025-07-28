package com.wevx.dealershipmanagement.domain.models

data class LoginModel(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val userPhoneNumber: String,
    val userRole: String,
    val userProfileImageUrl: String? = null,
    val nid: String? = null,
    val refreshToken: String? = null,
    val accessToken: String? = null
)
