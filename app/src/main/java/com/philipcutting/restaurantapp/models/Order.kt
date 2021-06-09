package com.philipcutting.restaurantapp.models

import java.time.Instant

data class Order(
    val menuItems: MutableList<MenuItem>,

    val time_in_minutes: Double = 0.0,
    val timeInitiated: Instant? = null,

    val orderPickedUp: Boolean = false
)
