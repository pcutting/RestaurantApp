package com.philipcutting.restaurantapp.models

import java.time.Instant


object Order {
    var menuItems: MutableList<MenuItem>  = mutableListOf()
    var time_in_minutes: Double = 0.0
    var timeInitiated: Instant? = null
    var orderPickedUp: Boolean = false

    fun clear() {
        menuItems.clear()
        menuItems = mutableListOf()
    }


}
