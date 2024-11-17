package ru.itis.homeworks.application2.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.databinding.ItemSongBinding
import ru.itis.homeworks.application2.utils.MyDiffUtil

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

    fun updateData(newList: List<Song>) {
        val diffCallback = MyDiffUtil(
            oldList = list,
            newList = newList
        )
        //Diff.Result - это та самая последовательность преобразований, которая приведет старый лист к новому
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        //после проведения преобразований нам нужно уведомить адаптер, что преобразования вычислены и ему нужно
        //их применить
        diffResult.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(newList)
    }
}



/*package ru.itis.homeworks.application2.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.databinding.ItemSongBinding
import ru.itis.homeworks.application2.databinding.ItemGridSongBinding

class SongAdapter(
    items: List<Song>,
    private val glide: RequestManager,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<SongHolder>() {

    private val list = mutableListOf<Song>()
    private var flag = false

    init {
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun setLayoutManagerType(isGrid: Boolean) {
        notifyDataSetChanged()
        this.flag = isGrid
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongHolder {
        val binding = if (flag) {
            ItemGridSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return SongHolder(
            viewBinding = binding,
            glide = glide,
            onClick = onClick
        )
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}*/