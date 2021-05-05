package com.philipcutting.restaurantapp.models

data class MenuItems(
    val items: List<MenuItem>
)

data class MenuItem(
    val id: Int,
    val name: String,
    val detailText: String,
    val price: Double,
    val category: String,
    val imageUrl: String
)