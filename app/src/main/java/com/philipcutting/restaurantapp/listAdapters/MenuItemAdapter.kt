package com.philipcutting.restaurantapp.listAdapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.philipcutting.restaurantapp.databinding.MenuItemBinding
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.serverApi.MenuRepository
import com.philipcutting.restaurantapp.utilities.toCurrencyFormatFromDouble


private const val TAG = "MenuItemsAdapter"

class MenuItemsAdapter(val onClick: (MenuItem) -> Unit): ListAdapter<MenuItem, MenuItemsAdapter.MenuItemsViewHolder>(diff) {

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

    fun removeItemAt(position: Int) {
        notifyItemRemoved(position)
    }

    class MenuItemsViewHolder(
            private val binding: MenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MenuItem, onClick: (MenuItem) -> Unit) {
            binding.itemName.text = item.name
            binding.priceTv.text = item.price.toCurrencyFormatFromDouble()
            binding.menuItemContainer.setOnClickListener {
                onClick(item)
            }
            MenuRepository.loadImage(item.imageUrl){ bitmap ->
                Log.i(TAG, "Bitmap -> $bitmap.toString()")
                binding.imageView.setImageBitmap(bitmap)
                //binding.materialCardview.drawToBitmap(bitmap)
            }
        }
    }
}