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
import com.philipcutting.restaurantapp.respositories.MenuRepository
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel

private const val TAG = "MenuItemsFragment"

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



        MenuRepository.fetchMenuItems(category){
            adapter.submitList(it)
            Log.i(TAG, "fetched #:${it.count()}")
        }

        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Menu Items: $category"
    }

    private  val onItemClick: (item: MenuItem) -> Unit = { item ->
        viewModel.goToMenuItemDetailFragment(item)
    }
}