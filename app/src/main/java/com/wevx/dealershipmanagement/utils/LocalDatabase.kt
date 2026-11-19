package com.wevx.dealershipmanagement.utils

import com.wevx.dealershipmanagement.R
import com.wevx.dealershipmanagement.domain.models.Divisions
import com.wevx.dealershipmanagement.domain.models.DrawerItems
import com.wevx.dealershipmanagement.domain.models.Products

object LocalDatabase {


    val drawerImage = listOf(
        R.drawable.ic_home_24,
        R.drawable.ic_stock_availibity_24,
        R.drawable.ic_visit_planners_24,
        R.drawable.ic_payment_collection_24,
        R.drawable.ic_payment_history_24,
        R.drawable.ic_edit_profile,
        R.drawable.ic_change_password,
        R.drawable.ic_sync_data_24
    )

    val drawerItems = listOf(
        DrawerItems(drawerImage[0], "Home"),
        DrawerItems(drawerImage[1], "Stock Availability"),
        DrawerItems(drawerImage[2], "Pending Delivery"),
        DrawerItems(drawerImage[3], "Sales Summary"),
        DrawerItems(drawerImage[4], "Payment History"),
        DrawerItems(drawerImage[5], "Edit Profile"),
        DrawerItems(drawerImage[6], "Change Password")
    )


    val divisions = listOf(
        Divisions(1, "Dhaka"),
        Divisions(2, "Chattogram"),
        Divisions(3, "Rajshahi"),
        Divisions(4, "Khulna"),
        Divisions(5, "Barishal"),
        Divisions(6, "Sylhet"),
        Divisions(7, "Rangpur"),
        Divisions(8, "Mymensingh")
    )

    val divisionList = listOf("Dhaka", "Chattogram", "Rajshahi", "Khulna", "Barishal", "Sylhet", "Rangpur", "Mymensingh")

}