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
        DrawerItems(drawerImage[2], "Today's Delivery"),
        DrawerItems(drawerImage[3], "Payment Collection"),
        DrawerItems(drawerImage[4], "Payment History"),
        DrawerItems(drawerImage[5], "Edit Profile"),
        DrawerItems(drawerImage[6], "Change Password")
    )

    val products = listOf(
        Products("1", "Banana", 10.00, 10.00, "pcs", "Fruit", "Local Farmer", ""),
        Products("2", "Apple", 25.00, 25.00, "pcs", "Fruit", "FreshFarm", ""),
        Products(
            "3",
            "Shampoo 200ml",
            120.00,
            120.00,
            "bottle",
            "Personal Care",
            "Sunsilk",
            ""
        ),
        Products(
            "4",
            "Toothpaste 100g",
            45.00,
            45.00,
            "tube",
            "Personal Care",
            "Pepsodent",
            ""
        ),
        Products("5", "Lux Soap 75g", 30.00, 30.00, "pcs", "Personal Care", "Lux", ""),
        Products("6", "Rice 5kg", 350.00, 350.00, "bag", "Grocery", "ACI", ""),
        Products("7", "Noodles Pack", 25.00, 25.00, "pack", "Food", "Maggi", ""),
        Products("8", "Mineral Water 1L", 20.00, 20.00, "bottle", "Beverage", "Mum", ""),
        Products("9", "Cooking Oil 1L", 165.00, 165.00, "bottle", "Grocery", "Rupchanda", ""),
        Products("10", "Ball Pen", 8.00, 8.00, "pcs", "Stationery", "Matador", ""),
        Products(
            "1",
            "Banana",
            10.00,
            10.00,
            "pcs",
            "Fruit",
            "Walton",
            "data:image/jpeg;base64,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+01MUjlDzZqdivvo9T8CebNTsV99HqfgXV6JqYpHKHmxU7DffR6n4E82anYr76PU/Aur0TU+RSOUPNmp2K++j1PwLzzZqdivvo9T8C6wRNT5FI5Q82anYr76PU/AqF3gvNNzVba6ptmM1Sk9gk7CXNAnuXWywHy30gcKeeLa1Ej2moG/Y4pqfIpGiqHJ9z2hzLS8e06hzaL3NI6w4Mgr782avYr76PU/AulOQ1EMw2zA7NSPvcxrj9ZKnE1PkUjk/wA2KvYb76PU/Auk+QtEsw2zY5rmltvTBa4Q4ENGjgdip1EbbJoIiKAEREAREQGD+TPk7cWbr012BorXHOU4c10tJfvlOh1CzhEQBERAEREAREQBERAEREAREQBERAEREAWK+UzBq15h9ShbtDqjn0iAXBohlRjjqdNgVlSICP5P2zqVpb03iHMo02OEgwWsaCJGh1CkERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQH/9k="
        ),
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