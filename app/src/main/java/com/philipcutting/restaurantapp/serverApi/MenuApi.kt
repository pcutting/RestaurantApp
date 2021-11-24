package com.philipcutting.restaurantapp.serverApi


import retrofit2.Call
import retrofit2.http.*

interface MenuApi {

    @GET("/categories")
    fun fetchCategories(): Call<Categories>

    @GET("/menu")
    fun fetchMenuItems(@Query("category") categoryName: String): Call<MenuItems>

    @POST("/order")
    fun submitOrder(@Body menuIds: MenuIds): Call<PrepTime>

}