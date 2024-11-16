package ru.itis.homeworks.application2.utils

import androidx.recyclerview.widget.DiffUtil
import ru.itis.homeworks.application2.recycler_view.Song

class DiffUtil(
    private val oldList: List<Song>,
    private val newList: List<Song>
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

        return oldItem.lyrics == newItem.lyrics
    }
}