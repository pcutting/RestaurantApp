package com.philipcutting.restaurantapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.philipcutting.restaurantapp.databinding.FragmentMenuItemsBinding
import com.philipcutting.restaurantapp.listAdapters.MenuItemsAdapter
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.serverApi.Categories
import com.philipcutting.restaurantapp.serverApi.MenuItems
import com.philipcutting.restaurantapp.serverApi.MenuRepository
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel
import retrofit2.Call


class MenuItemsFragment: Fragment(R.layout.fragment_menu_items) {

    companion object {
        private const val TAG = "MenuItemsFragment"

        private const val CATEGORY = "CATEGORY"

        fun newInstance(category: String): MenuItemsFragment {
            val bundle = Bundle().apply {
                putString(CATEGORY, category)
            }
            return MenuItemsFragment().apply {
                arguments = bundle
            }
        }
    }

    private lateinit var binding: FragmentMenuItemsBinding
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category:String = this.arguments?.get(CATEGORY).toString()

        val adapter = MenuItemsAdapter(onItemClick)
        binding = FragmentMenuItemsBinding.bind(view)
        binding.menuItemList.adapter = adapter
        binding.menuItemList.layoutManager = LinearLayoutManager(
                view.context,
                RecyclerView.VERTICAL,
                false
        )

        val onSuccess: (List<MenuItem>) -> Unit = {
            binding.errorsFragment.errorsView.visibility = View.GONE
            adapter.submitList(it)
            Log.i(TAG, "fetched #:${it.count()}")
        }


        val onError: (Call<MenuItems>, Throwable) -> Unit = { call, t ->
            Log.i(TAG, "Error message: ${t.message}")
            binding.errorsFragment.errorsView.visibility = View.VISIBLE
            binding.errorsFragment.header.text = "There was an error accessing these menu items."
            binding.errorsFragment.message.text = "Server Response: ${call.toString()}"
            binding.errorsFragment.callData.text = "'${t.message}'"
        }

        MenuRepository.fetchMenuItems(category,onSuccess, onError)

        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Menu Items: $category"
    }

    private  val onItemClick: (item: MenuItem) -> Unit = { item ->
        viewModel.goToMenuItemDetailFragment(item)
    }
}