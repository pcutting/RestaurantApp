package com.philipcutting.restaurantapp.respositories

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MenuApi {

    @GET("/categories")
    fun fetchCategories(): Call<Categories>

    @GET("/menu")
    fun fetchMenuItems(@Query("category") categoryName: String): Call<MenuItems>

    @POST("/order")
    fun submitOrder(@Body menuIds: List<Int>): Call<Int>

}