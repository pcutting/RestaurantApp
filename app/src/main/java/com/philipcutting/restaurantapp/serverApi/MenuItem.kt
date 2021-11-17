package com.philipcutting.restaurantapp.serverApi


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenuItems(
    val items: List<MenuItem>
)

@JsonClass(generateAdapter = true)
data class MenuItem(
    val id: Int,
    val name: String,
    @Json(name = "description") val detailText: String,
    val price: Double,
    val category: String,
    @Json(name = "image_url") val imageUrl: String
)
