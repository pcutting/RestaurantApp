package com.philipcutting.restaurantapp.serverApi


import com.philipcutting.restaurantapp.models.MenuIds
import retrofit2.Call
import retrofit2.http.*

interface MenuApi {

    @GET("/categories")
    fun fetchCategories(): Call<Categories>

    @GET("/menu")
    fun fetchMenuItems(@Query("category") categoryName: String): Call<MenuItems>

//    @Headers("Content-Type: application/json")
    @POST("/order")
    fun submitOrder(@Body menuIds: MenuIds): Call<PrepTime>

}