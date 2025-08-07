package com.wevx.dealershipmanagement.domain.models

data class PendingOrderModel(
    val id: String,
    val customerId: String,
    val salesmanId: String,
    val invoiceNumber: String,
    val shipmentDate: String,
    val paymentStatus: String,
    val shipmentAddress: String,
    val totalPrice: String,
    val due: String,
)