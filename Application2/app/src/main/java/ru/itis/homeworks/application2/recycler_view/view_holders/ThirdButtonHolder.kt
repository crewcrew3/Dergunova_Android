package ru.itis.homeworks.application2.recycler_view.view_holders

import androidx.recyclerview.widget.RecyclerView
import ru.itis.homeworks.application2.databinding.ItemThirdButtonBinding

class ThirdButtonHolder(
    private val onThirdButtonClick: () -> Unit,
    viewBinding: ItemThirdButtonBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.apply {
            buttonSuperGrid.setOnClickListener {
                onThirdButtonClick()
            }
        }
    }
}