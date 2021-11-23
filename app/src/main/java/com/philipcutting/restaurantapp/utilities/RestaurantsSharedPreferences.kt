package com.philipcutting.restaurantapp.utilities

import android.content.Context
import com.philipcutting.restaurantapp.models.MenuItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object RestaurantsSharedPreferences{
    private const val FILE_NAME = "com.philipcutting.restaurants_app.shared_preferences_file"
    private const val ORDER_JSON = "ORDER_JSON"

    fun saveOrder(context: Context, order: List<MenuItem>){
        val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString(ORDER_JSON, orderToJson(order))
            apply()
        }
    }

    fun getSavedOrder(context: Context): List<MenuItem> {
        val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val orderValueAsJsonString: String? = preferences.getString(ORDER_JSON, null)
        return if (orderValueAsJsonString.isNullOrEmpty()) {
                listOf()
            } else {
                orderFromJson(orderValueAsJsonString)
            }
    }
}
