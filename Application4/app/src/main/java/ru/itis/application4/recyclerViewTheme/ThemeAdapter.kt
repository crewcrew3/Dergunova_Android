package ru.itis.application4.recyclerViewTheme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.itis.application4.databinding.ItemThemeBinding

class ThemeAdapter(
    items: List<Theme>,
    private val onClick: (Theme) -> Unit
) : RecyclerView.Adapter<ThemeHolder>() {

    private val list = mutableListOf<Theme>()

    init {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThemeHolder = ThemeHolder(
        viewBinding = ItemThemeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onClick = onClick
    )

    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}