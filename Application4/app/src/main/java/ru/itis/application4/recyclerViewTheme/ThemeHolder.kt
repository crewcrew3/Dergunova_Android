package ru.itis.application4.recyclerViewTheme

import androidx.recyclerview.widget.RecyclerView
import ru.itis.application4.databinding.ItemThemeBinding

class ThemeHolder(
    private val viewBinding: ItemThemeBinding,
    private val onClick: (Theme) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var theme: Theme? = null

    init {
        viewBinding.apply {
            root.setOnClickListener {
                theme?.let { safeTheme ->
                    onClick.invoke(safeTheme)
                }
            }
        }
    }

    fun onBind(theme: Theme) {
        this.theme = theme
        viewBinding.apply {
            root.setBackgroundColor(
                theme.color
            )
        }
    }
}
