package com.philipcutting.restaurantapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.models.MenuIds
import com.philipcutting.restaurantapp.serverApi.MenuRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapter
import java.time.Instant
import kotlin.reflect.typeOf

private const val TAG = "MainActivityViewModel"
class MainActivityViewModel : ViewModel(){
    val categoryNavEvent: MutableLiveData<String> = MutableLiveData("")
    val menuItemNavEvent: MutableLiveData<MenuItem?> = MutableLiveData(null)
    val order: MutableLiveData<MutableList<MenuItem>> = MutableLiveData(mutableListOf())
    var itemsOrdered = MutableLiveData<Int>(0)

    var time_in_seconds:  LiveData<Double> =  MutableLiveData(0.0)
    var timeInitiated:  LiveData<Instant?> =  MutableLiveData(null)
    var orderPickedUp:  LiveData<Boolean> =  MutableLiveData(false)

    fun goToMenuFragment(category: String) {
        (categoryNavEvent as MutableLiveData).value = category
    }

    fun goToMenuItemDetailFragment(item: MenuItem) {
        (menuItemNavEvent as MutableLiveData).value = item
    }

    fun addItemToOrder(item: MenuItem){
        order.value?.add(item)
        itemsOrdered.value = order.value?.size ?: 0
        Log.i(TAG, "json: ${orderToJson()}")
    }

    fun deleteItemInOrder(index: Int){
        order.value?.removeAt(index)
    }

    fun getTimeForPickup(onSuccessOrder: (Int) -> Unit){
        Log.i(TAG, "getTimeForPickup called")
        val orderIds: List<Int> = order.value?.map {it.id } ?: emptyList()
        MenuRepository.submitOrder(orderIds, onSuccessOrder)
    }

    fun clearOrders(){
        order.value?.clear()
        order.value = mutableListOf()
        itemsOrdered.value = 0
    }

    fun orderToJson(): String {
        var json = ""


        val moshi: Moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, MenuItem::class.java)
        val jsonAdapter: JsonAdapter<List<MenuItem>> = moshi.adapter(type)

        json = jsonAdapter.toJson(order.value)

        return json
    }
}