package com.philipcutting.restaurantapp.serverApi

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Categories(
    val categories: List<String>
)
