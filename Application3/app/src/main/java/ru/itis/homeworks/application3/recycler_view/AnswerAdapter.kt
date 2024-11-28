package ru.itis.homeworks.application3.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.itis.homeworks.application3.databinding.ItemAnswerBinding
import ru.itis.homeworks.application3.models.Answer
import ru.itis.homeworks.application3.utils.AnswerDiffCallback

class AnswerAdapter(
    items: List<Answer>,
) : ListAdapter<Answer, AnswerViewHolder>(AnswerDiffCallback()) {

    private val list = mutableListOf<Answer>()

    init {
        list.addAll(items)
        submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnswerViewHolder = AnswerViewHolder(
        viewBinding = ItemAnswerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        ),
        onClick = ::onClick
    )

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    private fun onClick(answer: Answer) {
        if (answer.isSelected) {
            answer.isSelected = false
        } else {
            list.forEach { it.isSelected = false }
            answer.isSelected = true
        }
        notifyDataSetChanged()
    }
}
