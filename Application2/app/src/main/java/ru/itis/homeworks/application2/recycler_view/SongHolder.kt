package ru.itis.homeworks.application2.recycler_view

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.ItemSongBinding

class SongHolder(
    private val viewBinding: ItemSongBinding,
    private val glide: RequestManager,
    private val onClick: (Song) -> Unit
) : ViewHolder(viewBinding.root) {

    fun onBind(song: Song) {
        viewBinding.apply {
            tvTitle.text = song.name
            glide
                .load(song.url)
                .error(R.drawable.pic_error)
                .placeholder(R.drawable.pic_empty)
                .into(ivImage)
            root.setOnClickListener {
                onClick.invoke(song)
            }
        }
    }
}
