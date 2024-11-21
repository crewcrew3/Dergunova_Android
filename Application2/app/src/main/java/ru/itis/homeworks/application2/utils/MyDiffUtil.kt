package ru.itis.homeworks.application2.utils

import androidx.recyclerview.widget.DiffUtil
import ru.itis.homeworks.application2.recycler_view.MultipleHolderData
import ru.itis.homeworks.application2.recycler_view.SongHolderData

class MyDiffUtil(
    private val oldList: List<MultipleHolderData>,
    private val newList: List<MultipleHolderData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem is SongHolderData && newItem is SongHolderData) {
            return oldItem.lyrics == newItem.lyrics
        }
        return oldItem.name == newItem.name
    }
}