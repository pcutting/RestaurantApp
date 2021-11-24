package com.philipcutting.restaurantapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.philipcutting.restaurantapp.databinding.FragmentOrderBinding
import com.philipcutting.restaurantapp.listAdapters.MenuItemsAdapter
import com.philipcutting.restaurantapp.listAdapters.SwipeToDeleteCallback
import com.philipcutting.restaurantapp.models.MenuItem
import com.philipcutting.restaurantapp.serverApi.Categories
import com.philipcutting.restaurantapp.serverApi.MenuItems
import com.philipcutting.restaurantapp.serverApi.PrepTime
import com.philipcutting.restaurantapp.viewmodels.MainActivityViewModel
import retrofit2.Call
import java.time.Instant

class OrderFragment: Fragment(R.layout.fragment_order) {

    companion object {
        private const val TAG = "OrderFragment"
    }

    private lateinit var binding: FragmentOrderBinding
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MenuItemsAdapter(onItemClick)
        binding = FragmentOrderBinding.bind(view)
        binding.menuItemList.adapter = adapter
        binding.menuItemList.layoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        val swipeHandler = object : SwipeToDeleteCallback(view.context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipeAdapter = adapter
                val position = viewHolder.adapterPosition
                viewModel.deleteItemInOrder(position, requireContext())
                swipeAdapter.removeItemAt(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.menuItemList)

        val onSuccess: (Int) -> Unit = {cookTimeInMinutes ->
            Log.i(TAG, "onSuccessOrder callback:  time in minutes found $cookTimeInMinutes")
            val intent = ConfirmationActivity.createIntent(requireContext(), cookTimeInMinutes, Instant.now())
            viewModel.clearOrders(requireContext())
            startActivity(intent)
        }

        val onError: (Call<PrepTime>, Throwable) -> Unit = { call, t ->
            Log.i(TAG, "Error message: ${t.message}")
            binding.errorsFragment.errorsView.visibility = View.VISIBLE
            binding.errorsFragment.header.text = "There was an error ordering the items."
            binding.errorsFragment.message.text = "Server Response: ${call.toString()}"
            binding.errorsFragment.callData.text = "'${t.message}'"
        }

        binding.purchaseButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Your order is almost ready.")
                .setNegativeButton("Cancel") { _, _ ->
                    Log.i(TAG, "Dialog cancel button pressed")
                }
                .setPositiveButton("submit") { _, _ ->
                    Log.i(TAG, "Clicked submit in purchase Dialog.")
                    viewModel.getTimeForPickup(onSuccess, onError)
                }.show()
        }

        viewModel.order.observe(viewLifecycleOwner) {
            if (it.size == 0) {
                binding.purchaseButton.visibility = View.INVISIBLE
            } else {
                binding.purchaseButton.visibility = View.VISIBLE
            }
            adapter.submitList(it)
        }

        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Order"
    }

    private  val onItemClick: (item: MenuItem) -> Unit = { item ->
        viewModel.goToMenuItemDetailFragment(item)
    }
}