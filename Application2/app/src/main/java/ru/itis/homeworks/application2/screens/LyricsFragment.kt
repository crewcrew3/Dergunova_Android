package ru.itis.homeworks.application2.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.itis.homeworks.application2.Properties
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.FragmentLyricsBinding
import ru.itis.homeworks.application2.recycler_view.Song
import ru.itis.homeworks.application2.recycler_view.SongDatabase

class LyricsFragment : Fragment(R.layout.fragment_lyrics) {

    private var viewBinding: FragmentLyricsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentLyricsBinding.bind(view)

        val id = arguments?.getInt(Properties.ARG_ID)
        val song = SongDatabase.songs.find{it.id == id} ?: return

        viewBinding?.apply {
            tvTitle.text = song.name
            tvLyrics.text = song.lyrics

            Glide.with(this@LyricsFragment)
                .load(song.url)
                .error(R.drawable.pic_error)
                .placeholder(R.drawable.pic_empty)
                .into(ivImage)
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    companion object {
        fun getInstance(
            id: Int
        ): LyricsFragment {
            val bundle = Bundle()
            bundle.putInt(Properties.ARG_ID, id)
            return LyricsFragment().apply {
                arguments = bundle
            }
        }
    }
}