package com.philipcutting.restaurantapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.philipcutting.restaurantapp.databinding.FragmentCategoryBinding
import com.philipcutting.restaurantapp.listAdapters.CategoriesAdapter
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.serverApi.Categories
import com.philipcutting.restaurantapp.serverApi.MenuRepository
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel
import retrofit2.Call

private const val TAG = "CategoryFragment"

class CategoryFragment: Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCategoryBinding.bind(view)

        val adapter = CategoriesAdapter(onCategoryClick)
        binding.categoryList.adapter = adapter
        binding.categoryList.layoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        val onSuccess: (List<String>?) -> Unit = { categories ->
            adapter.submitList(categories)
            binding.errorsFragment.errorsView.visibility = View.GONE
        }

        val onError: (Call<Categories>, Throwable) -> Unit = { call, t ->
            Log.i(TAG, "Error message: ${t.message}")
            binding.errorsFragment.errorsView.visibility = View.VISIBLE
            binding.errorsFragment.header.text = "There was an error accessing Categories"
            binding.errorsFragment.message.text = "Server Response: ${call.toString()}"
            binding.errorsFragment.callData.text = "'${t.message}'"
        }

        MenuRepository.fetchCategories (onSuccess, onError)

        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Categories"
    }

    private val onCategoryClick: (category: String) -> Unit = {
        viewModel.goToMenuFragment(it)
    }
}