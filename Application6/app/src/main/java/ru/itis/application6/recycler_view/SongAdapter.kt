package ru.itis.application6.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.itis.application6.data.entities.SongEntity
import ru.itis.application6.databinding.ItemSongBinding

class SongAdapter(
    items: List<SongEntity>,
    private val onLongClick: (SongEntity, Int) -> Unit,
    private val glide: RequestManager,
) : RecyclerView.Adapter<SongHolder>() {

    private val list = mutableListOf<SongEntity>()

    init {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongHolder = SongHolder(
        viewBinding = ItemSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        ),
        onLongClick = onLongClick,
        glide = glide,
    )

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}