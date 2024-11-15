package ru.itis.homeworks.application2.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.Properties
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.FragmentSongCatalogBinding
import ru.itis.homeworks.application2.recycler_view.Song
import ru.itis.homeworks.application2.recycler_view.SongAdapter
import ru.itis.homeworks.application2.recycler_view.SongDatabase

class SongCatalogFragment : Fragment(R.layout.fragment_song_catalog) {

    private var viewBinding: FragmentSongCatalogBinding? = null
    private var adapter: SongAdapter? = null
    private val containerId: Int = R.id.container

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentSongCatalogBinding.bind(view)

        val glide = Glide.with(this@SongCatalogFragment)
        initAdapter(glide)
    }

    private fun initAdapter(glide: RequestManager) {
        viewBinding?.apply {
            adapter = SongAdapter(
                items = SongDatabase.songs,
                glide = glide,
                onClick = ::onClick
            )
            rvSongs.adapter = adapter
            rvSongs.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onClick(song: Song) {
        parentFragmentManager.beginTransaction()
            .replace(containerId, LyricsFragment.getInstance(song.id), Properties.SECOND_TAG)
            .addToBackStack(Properties.BACK_FIRST_TAG)
            .commit()
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }
}