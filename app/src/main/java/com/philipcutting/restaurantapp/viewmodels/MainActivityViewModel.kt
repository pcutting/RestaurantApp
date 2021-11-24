package com.philipcutting.restaurantapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.serverApi.Categories
import com.philipcutting.restaurantapp.serverApi.MenuRepository
import com.philipcutting.restaurantapp.serverApi.PrepTime
import com.philipcutting.restaurantapp.utilities.RestaurantsSharedPreferences
import retrofit2.Call

private const val TAG = "MainActivityViewModel"
class MainActivityViewModel : ViewModel(){
    val categoryNavEvent: MutableLiveData<String> = MutableLiveData("")
    val menuItemNavEvent: MutableLiveData<MenuItem?> = MutableLiveData(null)
    var order: MutableLiveData<MutableList<MenuItem>> = MutableLiveData(mutableListOf())
        set(value) {
            field = value
            updateCount()
        }
    var itemsOrdered = MutableLiveData(0)

    private fun updateCount() {
        itemsOrdered.value = order.value?.size ?: 0
    }

    fun checkForSavedOrder(context: Context){
        order.value = RestaurantsSharedPreferences.getSavedOrder(context).toMutableList()
        updateCount()
    }

    fun goToMenuFragment(category: String) {
        (categoryNavEvent as MutableLiveData).value = category
    }

    fun goToMenuItemDetailFragment(item: MenuItem) {
        (menuItemNavEvent as MutableLiveData).value = item
    }

    fun addItemToOrder(item: MenuItem, context: Context){
        order.value?.add(item)
        updateCount()
        RestaurantsSharedPreferences.saveOrder(context,order.value ?: emptyList())
    }

    fun deleteItemInOrder(index: Int, context: Context){
        order.value?.removeAt(index)
        updateCount()
        RestaurantsSharedPreferences.saveOrder(context,order.value ?: emptyList())
    }

    fun getTimeForPickup(onSuccessOrder: (Int) -> Unit, onError: (Call<PrepTime>, t: Throwable) -> Unit){
        Log.i(TAG, "getTimeForPickup called")
        val orderIds: List<Int> = order.value?.map {it.id } ?: emptyList()
        MenuRepository.submitOrder(orderIds, onSuccessOrder, onError)
    }

    fun clearOrders(context: Context){
        order.value?.clear()
        order.value = mutableListOf()
        RestaurantsSharedPreferences.saveOrder(context,order.value ?: emptyList())
        updateCount()
    }
}