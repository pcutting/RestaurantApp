package com.philipcutting.restaurantapp.serverApi

import com.philipcutting.restaurantapp.serverApi.MenuItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "menu_items") val menuItems: MutableList<MenuItem>
)
