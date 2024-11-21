package ru.itis.homeworks.application2.recycler_view.view_holders

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.ItemGridSongBinding
import ru.itis.homeworks.application2.recycler_view.SongHolderData
import ru.itis.homeworks.application2.recycler_view.MultipleHolderData

class GridHolder(
    private val viewBinding: ItemGridSongBinding,
    private val glide: RequestManager,
    private val onClick: (MultipleHolderData) -> Unit
) : ViewHolder(viewBinding.root) {

    fun onBind(song: SongHolderData) {
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