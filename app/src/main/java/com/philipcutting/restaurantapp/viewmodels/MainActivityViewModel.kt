package com.philipcutting.restaurantapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.philipcutting.restaurantapp.models.MenuItem

class MainActivityViewModel : ViewModel(){
    val categoryNavEvent: LiveData<String> = MutableLiveData("")
    val menuItemNavEvent: LiveData<MenuItem?> = MutableLiveData(null)

    fun goToMenuFragment(category: String) {
        (categoryNavEvent as MutableLiveData).value = category
    }

    fun goToMenuItemDetailFragment(item: MenuItem) {
        (menuItemNavEvent as MutableLiveData).value = item
    }

}