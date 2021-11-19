package com.philipcutting.restaurantapp.listAdapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.philipcutting.restaurantapp.R

abstract class SwipeToDeleteCallback(
    context: Context
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private val background = ColorDrawable()
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_sweep_24)
    private val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
    private val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        background.color = Color.parseColor("#F44336")

        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + intrinsicHeight

        deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        deleteIcon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}