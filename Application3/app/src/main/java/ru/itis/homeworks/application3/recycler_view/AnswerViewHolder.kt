package ru.itis.homeworks.application3.recycler_view

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.itis.homeworks.application3.R
import ru.itis.homeworks.application3.databinding.ItemAnswerBinding
import ru.itis.homeworks.application3.models.Answer

class AnswerViewHolder(
    private val viewBinding: ItemAnswerBinding,
    private val onClick : (Int) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.apply {
            root.setOnClickListener {
                onClick.invoke(adapterPosition)
            }
            radioButton.setOnClickListener {
                onClick.invoke(adapterPosition)
            }
        }
    }

    fun onBind(answer: Answer) {
        viewBinding.apply {
            tvAnswer.text = answer.text
            onBindSelectedItem(answer.isSelected)
        }
    }

    fun onBindSelectedItem(isSelected: Boolean) {
        viewBinding.apply {
            radioButton.isChecked = isSelected
            if (isSelected) {
                root.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.item_selected_color
                    )
                )
            } else {
                root.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.white))
            }
            radioButton.setButtonTintList(getColorStateList())
        }
    }

    private fun getColorStateList(): ColorStateList = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked), // Активное состояние
            intArrayOf(-android.R.attr.state_checked) // Нективное состояние
        ),
        intArrayOf(
            ContextCompat.getColor(
                viewBinding.root.context,
                R.color.radio_button_checked_color // Цвет при активном состоянии
            ),
            ContextCompat.getColor(
                viewBinding.root.context,
                R.color.default_color // Цвет при неактивном состоянии
            )
        )
    )
}
