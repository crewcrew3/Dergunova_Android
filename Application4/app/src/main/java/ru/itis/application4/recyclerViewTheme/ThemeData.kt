package ru.itis.application4.recyclerViewTheme

import android.content.Context
import androidx.core.content.ContextCompat
import ru.itis.application4.R

object ThemeData {

    fun getThemeList(context: Context): List<Theme> {
        return listOf(
            Theme(
                id = "RedTheme",
                color = ContextCompat.getColor(context, R.color.theme_red),
            ),
            Theme(
                id = "GreenTheme",
                color = ContextCompat.getColor(context, R.color.theme_green),
            ),
            Theme(
                id = "YellowTheme",
                color = ContextCompat.getColor(context, R.color.theme_yellow),
            )
        )
    }
}