package com.philipcutting.restaurantapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.philipcutting.restaurantapp.databinding.FragmentCategoryBinding
import com.philipcutting.restaurantapp.listAdapters.CategoriesAdapter
import com.philipcutting.restaurantapp.respositories.MenuResponsitory
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel
import java.lang.ClassCastException

class CategoryFragment: Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: MainActivityViewModel by activityViewModels()
//    private lateinit var listener: IMenu
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is IMenu) {
//            listener = context
//        } else {
//            throw ClassCastException(
//                context.toString() + " must implement IMenu"
//            )
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCategoryBinding.bind(view)


        var adapter = CategoriesAdapter(onCategoryClick)
        binding.categoryList.adapter = adapter
        binding.categoryList.layoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        MenuResponsitory.fetchCategories { categories ->
            adapter.submitList(categories)
        }

        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Categories"
    }

    private val onCategoryClick: (category: String) -> Unit = {
        viewModel.goToMenuFragment(it)
    }


}