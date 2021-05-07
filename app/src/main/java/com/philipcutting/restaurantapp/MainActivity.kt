package com.philipcutting.restaurantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.philipcutting.restaurantapp.databinding.ActivityMainBinding
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : MainActivityViewModel

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.categoryNavEvent.observe(this){ category ->
            if (!category.isNullOrBlank()) {
                onCategoryClick(category)
            }
        }


        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            handleBottomNavigation(it.itemId)
        }
        binding.bottomNavigation.selectedItemId = R.id.menu_categories



    }

    fun onCategoryClick(category: String) {
        val menu = MenuItemsFragment.newInstance(category)
        swapFragment(menu)
    }


    private fun handleBottomNavigation(menuItemId: Int) : Boolean {
        return when (menuItemId) {
            R.id.menu_categories -> {
                swapFragment(CategoryFragment())
                true
            }

            R.id.menu_cart -> {
                swapFragment(CartFragment())
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