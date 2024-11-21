package ru.itis.homeworks.application2.view_holders

import androidx.recyclerview.widget.RecyclerView
import ru.itis.homeworks.application2.databinding.ItemHolderButtonsBinding


class ButtonHolder(
    private val onLinearButtonClick: () -> Unit,
    private val onGridButtonClick: () -> Unit,
    viewBinding: ItemHolderButtonsBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.apply {
            buttonLinear.setOnClickListener {
                onLinearButtonClick()
            }

            buttonGrid.setOnClickListener {
                onGridButtonClick()
            }
        }
    }
}