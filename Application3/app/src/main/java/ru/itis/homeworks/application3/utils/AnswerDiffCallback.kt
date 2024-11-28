package ru.itis.homeworks.application3.utils

import androidx.recyclerview.widget.DiffUtil
import ru.itis.homeworks.application3.models.Answer

class AnswerDiffCallback : DiffUtil.ItemCallback<Answer>() {

    override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
        return oldItem.text == newItem.text
    }
}