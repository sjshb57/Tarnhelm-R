package cn.ac.lz233.tarnhelm.ui.rules

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragSwipeCallback(private val adapter: IDragSwipe) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled() = true

    override fun isItemViewSwipeEnabled() = false

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.bindingAdapterPosition
        val to = target.bindingAdapterPosition
        if (from == RecyclerView.NO_POSITION || to == RecyclerView.NO_POSITION) return false
        adapter.onItemSwapped(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) return
        when (direction) {
            ItemTouchHelper.END -> adapter.onItemCopy(position)
            ItemTouchHelper.START -> adapter.onItemDeleted(position)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                viewHolder.itemView.alpha = if (isCurrentlyActive) 0.6f else 1f
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            /*ItemTouchHelper.ACTION_STATE_SWIPE -> {
                val contentCardView: MaterialCardView = viewHolder.itemView.findViewById(R.id.ruleContentCardView)
                if (dX<0){// delete
                    contentCardView.translationX = dX
                }
            }*/
        }
    }
}