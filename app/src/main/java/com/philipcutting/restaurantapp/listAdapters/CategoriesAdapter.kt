package com.philipcutting.restaurantapp.listAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.philipcutting.restaurantapp.MenuItemsFragment
import com.philipcutting.restaurantapp.R
import com.philipcutting.restaurantapp.databinding.CategoryItemBinding
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel

class CategoriesAdapter(val onClick : (String) -> Unit) : ListAdapter<String, CategoriesAdapter.CategoryItemViewHolder>(diff) {


    companion object {
        private val diff = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.onBind(getItem(position),onClick)
    }

    class CategoryItemViewHolder(
        private val binding: CategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(title: String, onClick: (String) -> Unit) {
            binding.categoryTv.text = title
            binding.categoryContainer.setOnClickListener {
                onClick(title)
//                var menuFrag = MenuItemsFragment.newInstance(title)
//                parentFragmentManager.beginTransaction()
////                    .add(R.id.fragment_container,menuFrag)
//                    .replace(R.id.fragment_container,menuFrag)
//                    .commit()

            }
        }
    }
}
