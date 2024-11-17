package ru.itis.homeworks.application2.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.Properties
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.bottom_sheets.BottomSheetFragment
import ru.itis.homeworks.application2.databinding.FragmentSongCatalogBinding
import ru.itis.homeworks.application2.recycler_view.Song
import ru.itis.homeworks.application2.recycler_view.SongAdapter
import ru.itis.homeworks.application2.data.RecyclerViewListData
import ru.itis.homeworks.application2.data.SongDatabase
import kotlin.random.Random

class SongCatalogFragment : Fragment(R.layout.fragment_song_catalog) {

    private var viewBinding: FragmentSongCatalogBinding? = null
    private var adapter: SongAdapter? = null
    private val containerId: Int = R.id.container

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentSongCatalogBinding.bind(view)

        val glide = Glide.with(this@SongCatalogFragment)
        initAdapter(glide)

        viewBinding?.apply {

            fab.setOnClickListener {
                val dialog = BottomSheetFragment().apply {
                    isCancelable = true
                }
                dialog.show(childFragmentManager, Properties.TAG_BOTTOM_SHEET)
            }

            buttonGrid.setOnClickListener {
                rvSongs.layoutManager = GridLayoutManager(
                    requireContext(), 3, RecyclerView.VERTICAL, false
                )
                //adapter?.setLayoutManagerType(true)
            }

            buttonLinear.setOnClickListener {
                rvSongs.layoutManager = LinearLayoutManager(
                    requireContext(), RecyclerView.VERTICAL, false
                )
                //adapter?.setLayoutManagerType(false)
            }

        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    private fun initAdapter(glide: RequestManager) {
        viewBinding?.apply {
            adapter = SongAdapter(
                items = RecyclerViewListData.songs,
                glide = glide,
                onClick = ::onClick
            )
            rvSongs.adapter = adapter
            rvSongs.layoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
            )

            /*rvSongs.addItemDecoration(Decorator(
               margin = getValueInDp(value = 8f, requireContext()).toInt()
          ))*/
        }
    }

    private fun onClick(song: Song) {
        parentFragmentManager.beginTransaction()
            .replace(containerId, LyricsFragment.getInstance(song.id), Properties.DETAILED_TAG)
            .addToBackStack(Properties.BACK_DETAILED_TAG)
            .commit()
    }

    fun add(number: Int) {
        val rvList = RecyclerViewListData.songs
        val dataList = SongDatabase.songs
        repeat(number) {
            var position = 0
            if (rvList.size != 0) {
                position = generateRandomNumber(rvList.size)
            }
            val element = generateRandomNumber(dataList.size)
            rvList.add(position, dataList[element])
        }
        adapter?.updateData(rvList)
    }

    fun delete(number: Int) {
        var count = number
        val rvList = RecyclerViewListData.songs
        if (rvList.size < number) {
            count = rvList.size
        }
        repeat(count) {
            val position = generateRandomNumber(rvList.size)
            rvList.remove(rvList[position])
        }
        adapter?.updateData(rvList)
    }

    private fun generateRandomNumber(size: Int) : Int = Random.nextInt(0, size)
}