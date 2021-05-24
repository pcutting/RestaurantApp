package com.philipcutting.restaurantapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.philipcutting.restaurantapp.models.MenuItem

class MainActivityViewModel : ViewModel(){
    val categoryNavEvent: LiveData<String> = MutableLiveData("")
    val menuItemNavEvent: LiveData<MenuItem?> = MutableLiveData(null)
    private val menuItems: MutableList<MenuItem> = mutableListOf()

//    val currentItem: MenuItem? = null

    fun goToMenuFragment(category: String) {
        (categoryNavEvent as MutableLiveData).value = category
    }

    fun goToMenuItemDetailFragment(item: MenuItem) {
        (menuItemNavEvent as MutableLiveData).value = item
    }

//    fun getMenuItem(id:Int) = menuItems.first { it.id == id }
//    fun menuItem() = menuItemNavEvent.value
}