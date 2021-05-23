package com.philipcutting.restaurantapp.listAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.philipcutting.restaurantapp.databinding.MenuItemBinding
import com.philipcutting.restaurantapp.models.MenuItem


class MenuItemsAdapter(val onClick: (MenuItem) -> Unit): ListAdapter<MenuItem, MenuItemsAdapter.MenuItemsViewHolder>(MenuItemsAdapter.diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<MenuItem>() {
            override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MenuItemBinding.inflate(inflater, parent, false)
        return MenuItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuItemsViewHolder, position: Int) {
        holder.onBind(getItem(position),onClick)
    }

    class MenuItemsViewHolder(
            private val binding: MenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MenuItem, onClick: (MenuItem) -> Unit) {
            binding.ItemName.text = item.name

            binding.menuItemContainer.setOnClickListener {
                onClick(item)
            }
        }
    }
}