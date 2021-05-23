package com.philipcutting.restaurantapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.philipcutting.restaurantapp.databinding.FragmentMenuItemBinding
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel

private const val TAG = "MenuItemDetailFragment"
class MenuItemDetailFragment: Fragment(R.layout.fragment_menu_item) {

    companion object {
        private const val MENUITEM = "MENUITEM"

        fun newInstance(item: MenuItem): MenuItemDetailFragment {
            val bundle = Bundle().apply {
                putString(MENUITEM, item.id.toString())
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


        viewModel.menuItemNavEvent.observe(viewLifecycleOwner){ item ->
            if(item != null) {
                binding.apply{
                    nameTv.text = item.name
                    detailTextTv.text = item.detailText
//                    this.imageView....
                    this.priceTv.text = item.price.toString()


                }

            }
        }


        val menuItem = this.arguments?.get(MENUITEM)
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Item: $menuItem."
    }


}