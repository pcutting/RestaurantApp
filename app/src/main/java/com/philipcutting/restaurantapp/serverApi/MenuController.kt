package com.philipcutting.restaurantapp.serverApi

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import com.philipcutting.restaurantapp.models.MenuIds
import com.philipcutting.restaurantapp.models.MenuItem
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

private const val TAG = "MenuController"

object MenuRepository {
    private const val localHostIP = "192.168.0.101"
    private const val baseUrl = "http://$localHostIP:8090"

    private val logger = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

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

    fun loadImage(url: String, onLoad: (Bitmap) -> Unit) {
        //val url = "http://localhost:8090/images/3.png"
        //val baseUrl = "192.168.0.101"
        val replaceKey = "localhost"

        val correctedUrl = url.replace(replaceKey, localHostIP)
        val request = Request.Builder().url(correctedUrl).build()

        client.build().newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e(TAG, "loadImage onFail: $e")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
                    Log.i(TAG, "loadImage: Image.byteCount for $url -> ${bitmap?.byteCount}")
                    android.os.Handler(Looper.getMainLooper()).post(object : Runnable {
                        override fun run() {
                            if(bitmap != null) {
                                onLoad(bitmap)
                            } else {
                                Log.e(TAG, "There was an error on loading the image from the server.")
                            }
                        }
                    })
                } else {
                    Log.e(TAG, "loadImage Had an error in loading.  ${response.message}")
                }
            }
        })
    }

    fun fetchCategories(onSuccess: (List<String>) -> Unit) {
        menuApi.fetchCategories()
            .enqueue(object : Callback<Categories> {
                override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                    val categories = response.body()?.categories ?: emptyList()
                    onSuccess(categories)
                }
                override fun onFailure(call: Call<Categories>, t: Throwable) {
                    Log.v("Networking", "Error! $t")
                }

            })
    }

    fun submitOrder(menuIds: List<Int>, onSuccess: (Int) -> Unit) {
        val modifiedMenuIds = com.philipcutting.restaurantapp.serverApi.MenuIds(menuIds)
        menuApi.submitOrder(modifiedMenuIds)
            .enqueue(object : Callback<PrepTime> {

                override fun onResponse(call: Call<PrepTime>, response: Response<PrepTime>) {
                    val prepMinutes = response.body()?.prepTimeInMinutes ?: -1
                    Log.i(TAG, "preptime: $prepMinutes")
                    onSuccess(prepMinutes)
                }

                override fun onFailure(call: Call<PrepTime>, t: Throwable) {
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