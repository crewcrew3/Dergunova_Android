package ru.itis.homeworks.application2.recycler_view.view_holders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.ItemWideGridSongBinding
import ru.itis.homeworks.application2.recycler_view.MultipleHolderData
import ru.itis.homeworks.application2.recycler_view.SongHolderData

class WideGridHolder(
    private val viewBinding: ItemWideGridSongBinding,
    private val glide: RequestManager,
    private val onClick: (MultipleHolderData) -> Unit,
    private val onLongClick: (Int) -> Unit
) : ViewHolder(viewBinding.root) {

    private var song: SongHolderData? = null

    init {
        viewBinding.apply {
            root.setOnClickListener {
                song?.let { safeSong ->
                    onClick.invoke(safeSong)
                }
            }
            root.setOnLongClickListener {
                onLongClick.invoke(adapterPosition)
                true
            }
        }
    }

    fun onBind(song: SongHolderData) {
        this.song = song
        viewBinding.apply {
            tvTitle.text = song.name
            glide
                .load(song.url)
                .error(R.drawable.pic_error)
                .placeholder(R.drawable.pic_empty)
                .into(ivImage)
        }
    }
}