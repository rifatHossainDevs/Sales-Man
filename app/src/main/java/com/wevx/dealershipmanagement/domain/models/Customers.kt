package com.wevx.dealershipmanagement.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customers(
    val customerId: String,
    val shopName: String,
    val customerName: String,
    val customerAddress: String,
    val customerImg: String,
    val customerCity: String,
    val customerArea: String,
    val customerZone: String
) : Parcelable
