package com.philipcutting.restaurantapp

import com.philipcutting.restaurantapp.models.MenuItem

interface IMenu {

    fun onCategorySelected(category: String)

    fun onMenuItemSelected(item: MenuItem)

}