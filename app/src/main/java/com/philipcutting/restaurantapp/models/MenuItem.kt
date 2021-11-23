package com.philipcutting.restaurantapp.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenuItem(
    val id: Int,
    val name: String,
    val detailText: String,
    val price: Double,
    val category: String,
    val imageUrl: String
)
