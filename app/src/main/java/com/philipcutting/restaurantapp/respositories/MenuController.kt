package com.philipcutting.restaurantapp.respositories

import android.util.Log
import com.philipcutting.restaurantapp.models.MenuItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "MenuController"

object MenuResponsitory {
    private const val localHostIP = "192.168.0.101"
    private const val baseUrl = "http://$localHostIP:8090"

    private val logger = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY )

    private val client = OkHttpClient.Builder().addInterceptor(logger)
    private val menuApi: MenuApi
        get() {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(MenuApi::class.java)
        }


    fun fetchCategories(onSuccess: (List<String>) -> Unit) {
        menuApi.fetchCategories()
            .enqueue(object : Callback<Categories> {
                override fun onFailure(call: Call<Categories>, t: Throwable) {
                    Log.v("Networking", "Error! $t")
                }

                override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                    val categories = response.body()?.categories ?: emptyList()
                    onSuccess(categories)
                }
            })
    }

    fun submitOrder(menuIds: List<Int>, onSuccess: (Int) -> Unit) {
        menuApi.submitOrder(menuIds)
            .enqueue(object : Callback<Int> {

                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    val prepTime = response.body() ?: -1
                    onSuccess(prepTime)
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.v("Networking", "Error! $t")
                }
            })
    }

    fun fetchMenuItems(category: String, onSuccess: (List<MenuItem>) -> Unit) {
        menuApi.fetchMenuItems(category)
            .enqueue(object : Callback<MenuItems> {
                override fun onFailure(call: Call<MenuItems>, t: Throwable) {
                    Log.v("Networking", "Error! $t")
                }

                override fun onResponse(call: Call<MenuItems>, response: Response<MenuItems>) {
                      //val menuItems = response.body()?.items ?: emptyList()
                    if (response.body() != null) {
                        onSuccess(convertRepositoryMenuToModelMenu(response.body()))
                    }
                }
            })
    }

    private fun convertRepositoryMenuToModelMenu(jMenuItems: MenuItems?): List<com.philipcutting.restaurantapp.models.MenuItem>{


        val list = mutableListOf<com.philipcutting.restaurantapp.models.MenuItem>()

        if(jMenuItems==null) {
            return list
        }

        jMenuItems.items.forEach {
            val item = com.philipcutting.restaurantapp.models.MenuItem(
                    id=it.id,
                    name = it.name,
                    detailText = it.detailText,
                    price = it.price,
                    category = it.category,
                    imageUrl = it.imageUrl
            )
            list.add(item)
        }
        return list.toList()
    }
}