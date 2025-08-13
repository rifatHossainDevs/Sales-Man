package com.wevx.dealershipmanagement.domain.models

data class ProfileModel(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val nid: String,
    val avatar: String,
    val userType: String,
    val companyId : String
)
