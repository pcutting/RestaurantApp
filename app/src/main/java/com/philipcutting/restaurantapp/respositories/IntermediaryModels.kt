package com.philipcutting.restaurantapp.respositories

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Categories(
    val categories: List<String>
)

@JsonClass(generateAdapter = true)
data class PreparationTime(
    @Json(name = "preparation_time") val prepTime:Int
)