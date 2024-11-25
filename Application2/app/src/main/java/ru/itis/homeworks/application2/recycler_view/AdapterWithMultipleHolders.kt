package ru.itis.homeworks.application2.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.itis.homeworks.application2.R
import ru.itis.homeworks.application2.databinding.ItemGridSongBinding
import ru.itis.homeworks.application2.databinding.ItemHolderButtonsBinding
import ru.itis.homeworks.application2.databinding.ItemLinearSongBinding
import ru.itis.homeworks.application2.databinding.ItemNarrowGridSongBinding
import ru.itis.homeworks.application2.databinding.ItemThirdButtonBinding
import ru.itis.homeworks.application2.databinding.ItemWideGridSongBinding
import ru.itis.homeworks.application2.utils.MyDiffUtil
import ru.itis.homeworks.application2.recycler_view.view_holders.ButtonHolder
import ru.itis.homeworks.application2.recycler_view.view_holders.GridHolder
import ru.itis.homeworks.application2.recycler_view.view_holders.LinearHolder
import ru.itis.homeworks.application2.recycler_view.view_holders.NarrowGridHolder
import ru.itis.homeworks.application2.recycler_view.view_holders.ThirdButtonHolder
import ru.itis.homeworks.application2.recycler_view.view_holders.WideGridHolder

class AdapterWithMultipleHolders(
    items: List<MultipleHolderData>,
    private val glide: RequestManager,
    private val onClick: (MultipleHolderData) -> Unit,
    private val onLinearButtonClick: () -> Unit,
    private val onGridButtonClick: () -> Unit,
    private val onThirdButtonClick: () -> Unit,
    private val onLongClick: (Int) -> Unit,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<MultipleHolderData>()

    init {
        list.addAll(items)
        notifyDataSetChanged()
    }

    //здесь решается какой именно холдер будет отрисовываться
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_linear_song -> {
                LinearHolder(
                    viewBinding = ItemLinearSongBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    glide = glide,
                    onClick = onClick
                )
            }

            R.layout.item_grid_song -> {
                GridHolder(
                    viewBinding = ItemGridSongBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    glide = glide,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
            }

            R.layout.item_narrow_grid_song -> {
                NarrowGridHolder(
                    viewBinding = ItemNarrowGridSongBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    glide = glide,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
            }

            R.layout.item_wide_grid_song -> {
                WideGridHolder(
                    viewBinding = ItemWideGridSongBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    glide = glide,
                    onClick = onClick,
                    onLongClick = onLongClick
                )
            }

            R.layout.item_holder_buttons -> {
                ButtonHolder(
                    onLinearButtonClick = onLinearButtonClick,
                    onGridButtonClick = onGridButtonClick,
                    viewBinding = ItemHolderButtonsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            R.layout.item_third_button -> {
                ThirdButtonHolder(
                    onThirdButtonClick = onThirdButtonClick,
                    viewBinding = ItemThirdButtonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalStateException("Unknown holder")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position]) {
            is SongHolderData -> {
                val layoutManager = recyclerView.layoutManager
                if (isFirstGrid(layoutManager)) {
                    (holder as? GridHolder)?.onBind(song = list[position] as SongHolderData)
                } else if (isSecondGrid(layoutManager)) {
                    if ((position + 1) % 3 == 0) {
                        (holder as? WideGridHolder)?.onBind(song = list[position] as SongHolderData)
                    } else {
                        (holder as? NarrowGridHolder)?.onBind(song = list[position] as SongHolderData)
                    }
                } else {
                    (holder as? LinearHolder)?.onBind(song = list[position] as SongHolderData)
                }
            }
            else -> Unit
        }
    }

    override fun getItemCount(): Int = list.size

    //отвечает за присвоение типа вью холдеру
    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return when (item) {
            is SongHolderData -> {
                val layoutManager = recyclerView.layoutManager
                if (isFirstGrid(layoutManager)) {
                    return R.layout.item_grid_song
                } else if (isSecondGrid(layoutManager)) {
                    if ((position + 1) % 3 == 0) {
                        R.layout.item_wide_grid_song
                    } else {
                        R.layout.item_narrow_grid_song
                    }
                } else {
                    R.layout.item_linear_song
                }
            }
            is ButtonHolderData -> {
                if (item.isThird) {
                    R.layout.item_third_button
                } else {
                    R.layout.item_holder_buttons
                }
            }
        }
    }

    fun updateData(newList: List<MultipleHolderData>) {
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

    fun addItem(newItem: MultipleHolderData, position: Int) {
        list.add(position, newItem)
        notifyItemInserted(position)
    }

    fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun isFirstGrid(layoutManager: RecyclerView.LayoutManager?) : Boolean {
        return (layoutManager is GridLayoutManager) && (layoutManager.spanCount == 3)
    }

    private fun isSecondGrid(layoutManager: RecyclerView.LayoutManager?) : Boolean {
        return (layoutManager is GridLayoutManager) && (layoutManager.spanCount == 2)
    }
}