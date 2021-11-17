package com.philipcutting.restaurantapp.models

import java.time.Instant


object Order {

    private var _menuItems: MutableList<MenuItem>  = mutableListOf()
    var menuItems = _menuItems

    var time_in_minutes: Double = 0.0
    var timeInitiated: Instant? = null
    var orderPickedUp: Boolean = false

    fun clear() {
        menuItems.clear()
        menuItems = mutableListOf()
    }

    fun addItemToOrder(menuItem: MenuItem){
        _menuItems.add(menuItem)
        menuItems = _menuItems
    }

}
