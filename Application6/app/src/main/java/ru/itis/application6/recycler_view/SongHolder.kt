package ru.itis.application6.recycler_view

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.itis.application6.R
import ru.itis.application6.data.entities.SongEntity
import ru.itis.application6.databinding.ItemSongBinding

class SongHolder(
    private val viewBinding: ItemSongBinding,
    private val onLongClick : (SongEntity, Int) -> Unit,
    private val glide: RequestManager,
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var song: SongEntity? = null

    init {
        viewBinding.apply {
            root.setOnLongClickListener {
                song?.let { safeSong ->
                    onLongClick.invoke(safeSong, adapterPosition)
                    true
                }
                false
            }
        }
    }

    fun onBind(song: SongEntity) {
        this.song = song
        viewBinding.apply {
            tvSongName.text = song.songName
            tvSongAuthor.text = song.songAuthor ?: itemView.context.getString(R.string.text_unknown)
            tvSongYear.text = song.songYear?.toString() ?: itemView.context.getString(R.string.text_unknown)
            song.songPosterUrl?.let { posterUrl ->
                glide
                    .load(Uri.parse(posterUrl))
                    .error(R.drawable.pic_error)
                    .placeholder(R.drawable.pic_empty)
                    .into(ivPoster)
            } ?: glide.load(R.drawable.pic_empty).into(ivPoster)
        }
    }
}