package com.philipcutting.restaurantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.philipcutting.restaurantapp.databinding.ActivityMainBinding
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var viewModel : MainActivityViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onStart() {
        super.onStart()
        viewModel.checkForSavedOrder(this)
        Log.i(TAG, "onStart called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var navBadge = binding.bottomNavigation.getOrCreateBadge(R.id.menu_cart)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.categoryNavEvent.observe(this){ category ->
            if (!category.isNullOrBlank()) {
                onCategoryClick(category)
            }
        }

        viewModel.menuItemNavEvent.observe(this){item ->
            if(item != null) {
                onMenuItemClick(item)
            }
        }
        viewModel.menuItemNavEvent.observe(this){item ->
            if (item != null) {
                onItemClick(item)
            }
        }

        viewModel.itemsOrdered.observe(this){
            Log.i("MainActivity", "onCreate: itemsOrdered: $it")
            navBadge.number = it
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            handleBottomNavigation(it.itemId)
        }
        binding.bottomNavigation.selectedItemId = R.id.menu_categories
    }

    private fun onMenuItemClick(item: MenuItem) {
        val itemFrag = MenuItemDetailFragment.newInstance(item)
        swapFragment(itemFrag)
    }

    private fun onCategoryClick(category: String) {
        val menuFragment = MenuItemsFragment.newInstance(category)
        swapFragment(menuFragment)
    }

    private  fun onItemClick(item: MenuItem){
        val menuItemFragment = MenuItemDetailFragment.newInstance(item)
        swapFragment(menuItemFragment)
    }

    private fun handleBottomNavigation(menuItemId: Int) : Boolean {
        return when (menuItemId) {
            R.id.menu_categories -> {
                swapFragment(CategoryFragment())
                true
            }

            R.id.menu_cart -> {
                swapFragment(OrderFragment())
                true
            }

            //R.id.menu_pending_pickup -> {}

            else -> false
        }
    }



    private fun swapFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}