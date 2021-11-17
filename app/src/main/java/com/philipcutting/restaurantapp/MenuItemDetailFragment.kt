package com.philipcutting.restaurantapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.philipcutting.restaurantapp.databinding.FragmentMenuItemBinding
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.serverApi.MenuRepository
import com.philipcutting.restaurantapp.utilities.toCurrencyFormatFromDouble
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel

private const val TAG = "MenuItemDetailFragment"
class MenuItemDetailFragment: Fragment(R.layout.fragment_menu_item) {
    companion object {
        private const val MENU_ITEM_ID = "menuItemId"
        private const val MENU_NAME = "menuName"
        private const val MENU_DESCRIPTION = "menuDescription"
        private const val MENU_IMAGE_URL = "menuImageUrl"
        private const val MENU_PRICE = "menuPrice"

        fun newInstance(item: MenuItem): MenuItemDetailFragment {
            val bundle = Bundle().apply {
                putString(MENU_ITEM_ID, item.id.toString())
                putString(MENU_NAME, item.name)
                putString(MENU_DESCRIPTION, item.detailText)
                putString(MENU_IMAGE_URL, item.imageUrl)
                putString(MENU_PRICE, item.price.toCurrencyFormatFromDouble())
            }
            return MenuItemDetailFragment().apply {
                arguments = bundle
            }
        }
    }

    private lateinit var binding:FragmentMenuItemBinding
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenuItemBinding.bind(view)

        val itemId: String = this.arguments?.get(MENU_ITEM_ID).toString()
        val itemName: String = this.arguments?.get(MENU_NAME).toString()
        val itemDescription:String = this.arguments?.get(MENU_DESCRIPTION).toString()
        val itemImageUrl:String = this.arguments?.get(MENU_IMAGE_URL).toString()
        val itemPrice:String = this.arguments?.get(MENU_PRICE).toString()

        viewModel.menuItemNavEvent.observe(viewLifecycleOwner){ item ->
            if(item != null) {
                binding.apply{
                    nameLabel.text = item.name
                    detailedDescription.text = item.detailText
                    detailedDescription.movementMethod = ScrollingMovementMethod()
                    price.text = item.price.toCurrencyFormatFromDouble()
                }
            } else {
                binding.apply {
                    nameLabel.text = itemName
                    detailedDescription.text = itemDescription
                    price.text = itemPrice
                }
            }
        }

        MenuRepository.loadImage(itemImageUrl){ bitmap ->
            Log.i(TAG, "Bitmap -> $bitmap.toString()")
            binding.imageView.setImageBitmap(bitmap)
        }

        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Item: $itemName."
    }
}