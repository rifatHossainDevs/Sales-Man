package com.wevx.dealershipmanagement.domain.models

data class PendingOrderSellerModel(
    val id: String,
    val invoiceNumber: String,
    val address: String,
    val paymentStatus: String,
    val total: String,
    val customerId: String
)
