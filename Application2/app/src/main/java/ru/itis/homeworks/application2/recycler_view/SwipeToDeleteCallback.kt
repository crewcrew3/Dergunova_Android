package ru.itis.homeworks.application2.recycler_view

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.itis.homeworks.application2.data.RecyclerViewListData
import ru.itis.homeworks.application2.recycler_view.view_holders.LinearHolder

class SwipeToDeleteCallback (
    private val adapter: AdapterWithMultipleHolders?
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is LinearHolder) {
            makeMovementFlags(0, ItemTouchHelper.LEFT)
        } else {
            makeMovementFlags(0, 0)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val rvList = RecyclerViewListData.songs
        rvList.removeAt(viewHolder.adapterPosition)
        adapter?.updateData(rvList)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return .66f
    }

}