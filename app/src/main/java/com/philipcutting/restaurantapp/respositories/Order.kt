package com.philipcutting.restaurantapp.respositories

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "menu_items") val menuItems: MutableList<MenuItem>
)
