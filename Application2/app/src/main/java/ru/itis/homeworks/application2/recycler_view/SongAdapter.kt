package ru.itis.homeworks.application2.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.databinding.ItemSongBinding

class SongAdapter(
    items: List<Song>,
    private val glide: RequestManager,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<SongHolder>() {

    private val list = mutableListOf<Song>()

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
            false
        ),
        glide = glide,
        onClick = onClick
    )

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}