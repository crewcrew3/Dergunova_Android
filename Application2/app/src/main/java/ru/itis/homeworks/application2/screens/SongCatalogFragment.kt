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
import ru.itis.homeworks.application2.data.RecyclerViewListData
import ru.itis.homeworks.application2.data.SongDatabase
import ru.itis.homeworks.application2.decorators.Decorator
import ru.itis.homeworks.application2.recycler_view.AdapterWithMultipleHolders
import ru.itis.homeworks.application2.recycler_view.MultipleHolderData
import ru.itis.homeworks.application2.utils.getValueInDp
import kotlin.random.Random

class SongCatalogFragment : Fragment(R.layout.fragment_song_catalog) {

    private var viewBinding: FragmentSongCatalogBinding? = null
    private var adapter: AdapterWithMultipleHolders? = null
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
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    private fun initAdapter(glide: RequestManager) {
        viewBinding?.apply {
            adapter = AdapterWithMultipleHolders(
                items = RecyclerViewListData.songs,
                glide = glide,
                onClick = ::onClick,
                onLinearButtonClick = ::onLinearButtonClick,
                onGridButtonClick = ::onGridButtonClick,
                recyclerView = rvSongs
            )
            rvSongs.adapter = adapter
            rvSongs.layoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
            )

            rvSongs.addItemDecoration(
                Decorator(
                    margin = getValueInDp(value = 8f, requireContext()).toInt()
                )
            )
        }
    }

    private fun onClick(song: MultipleHolderData) {
        parentFragmentManager.beginTransaction()
            .replace(containerId, LyricsFragment.getInstance(song.id), Properties.DETAILED_TAG)
            .addToBackStack(Properties.BACK_DETAILED_TAG)
            .commit()
    }

    private fun onLinearButtonClick() {
        viewBinding?.rvSongs?.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
    }

    private fun onGridButtonClick() {
        val gridLayoutManager = GridLayoutManager(
            requireContext(), 3, RecyclerView.VERTICAL, false
        )
        viewBinding?.rvSongs?.layoutManager = gridLayoutManager

        //Чтобы кнопки не ломались при переключении в грид. Теперь они будут занимать место,
        //которое занимают все 3 столбца
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) {
                    gridLayoutManager.spanCount
                } else {
                    1
                }
            }
        }
    }

    fun add(number: Int) {
        val rvList = RecyclerViewListData.songs
        val dataList = SongDatabase.songs
        repeat(number) {
            //чтобы не затрагивать кнопки, будем вставлять элементы на позицию >0
            var position = 1
            if (rvList.size != 1) {
                position = Random.nextInt(1, rvList.size)
            }
            val element = Random.nextInt(0, dataList.size)
            rvList.add(position, dataList[element])
        }
        adapter?.updateData(rvList)
    }

    fun delete(number: Int) {
        var count = number
        val rvList = RecyclerViewListData.songs
        if (rvList.size < number) {
            count = rvList.size - 1 //размер листа без кнопок
        }
        repeat(count) {
            //генерация рандомной позиции начиная с 1 гарантирует, что мы случайно не удалим кнопки
            val position = Random.nextInt(1, rvList.size)
            rvList.remove(rvList[position])
        }
        adapter?.updateData(rvList)
    }
}