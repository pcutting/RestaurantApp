package com.philipcutting.restaurantapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

private const val TAG = "MenuItemsFragment    mnmb"

class MenuItemsFragment: Fragment(R.layout.fragment_menu_items) {

    companion object {
        private const val CATEGORY = "CATEGORY"

        fun newInstance(category: String): MenuItemsFragment {
            val bundle = Bundle().apply {
                putString(CATEGORY, category)
            }
            return MenuItemsFragment().apply {
                setArguments(bundle)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = this.arguments?.get(CATEGORY)
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Menu Items: $category"
    }



}