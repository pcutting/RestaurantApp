package com.philipcutting.restaurantapp.serverApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenuIds(
    @Json(name = "menuIds") val menuIds: List<Int>
)
