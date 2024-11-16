package ru.itis.homeworks.application2.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class Decorator (
    private val margin: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val holder = parent.getChildViewHolder(view)
        outRect.apply {
            left = margin
            right = margin
        }
    }
}