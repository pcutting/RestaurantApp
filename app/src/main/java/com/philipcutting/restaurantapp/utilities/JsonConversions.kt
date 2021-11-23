package com.philipcutting.restaurantapp.utilities

import com.philipcutting.restaurantapp.models.MenuItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

fun orderToJson(order: List<MenuItem>): String? {
    if (order.isEmpty()) {
        return null
    }

    val moshi: Moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(List::class.java, MenuItem::class.java)
    val jsonAdapter: JsonAdapter<List<MenuItem>> = moshi.adapter(type)
    return jsonAdapter.toJson(order)
}

fun orderFromJson(orderString: String) : List<MenuItem>{
    if(orderString.isEmpty()) {
        return emptyList()
    }
    val moshi: Moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(List::class.java, MenuItem::class.java)
    val jsonAdapter: JsonAdapter<List<MenuItem>> = moshi.adapter(type)
    val savedOrder : List<MenuItem>? = jsonAdapter.fromJson(orderString)
    return savedOrder ?: emptyList()
}