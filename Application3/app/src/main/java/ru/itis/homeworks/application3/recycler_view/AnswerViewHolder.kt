package ru.itis.homeworks.application3.recycler_view

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.itis.homeworks.application3.R
import ru.itis.homeworks.application3.databinding.ItemAnswerBinding
import ru.itis.homeworks.application3.models.Answer

class AnswerViewHolder(
    private val viewBinding: ItemAnswerBinding,
    private val onClick : (Answer) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var answer: Answer? = null

    init {
        viewBinding.apply {
            root.setOnClickListener {
                answer?.let { safeAnswer ->
                    onClick.invoke(safeAnswer)
                }
            }
            radioButton.setOnClickListener {
                answer?.let { safeAnswer ->
                    onClick.invoke(safeAnswer)
                }
            }
        }
    }

    fun onBind(answer: Answer) {
        this.answer = answer
        viewBinding.apply {
            tvAnswer.text = answer.text
            onBindRadioButton(answer.isSelected)
            onBindColor(answer.isSelected)
        }
    }

    fun onBindRadioButton(isSelected: Boolean) {
        viewBinding.radioButton.isChecked = isSelected
    }

    fun onBindColor(isSelected: Boolean) {
        viewBinding.apply {
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
