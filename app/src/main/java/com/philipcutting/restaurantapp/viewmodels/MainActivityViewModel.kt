package com.philipcutting.restaurantapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.philipcutting.restaurantapp.models.MenuItem
import java.time.Instant

class MainActivityViewModel : ViewModel(){
    val categoryNavEvent: MutableLiveData<String> = MutableLiveData("")
    val menuItemNavEvent: MutableLiveData<MenuItem?> = MutableLiveData(null)
    val order: MutableLiveData<MutableList<MenuItem>> = MutableLiveData(mutableListOf())
    var itemsOrdered = MutableLiveData<Int>(0)

    var time_in_minutes:  LiveData<Double> =  MutableLiveData(0.0)
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
    }

    fun deleteItemInOrder(index: Int){
        order.value?.removeAt(index)
    }

    fun clearOrders(){
        order.value?.clear()
    }
}