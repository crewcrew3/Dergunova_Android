package ru.itis.homeworks.application2.data

import ru.itis.homeworks.application2.recycler_view.ButtonHolderData
import ru.itis.homeworks.application2.recycler_view.MultipleHolderData

object RecyclerViewListData {

    val songs: MutableList<MultipleHolderData> = mutableListOf(
        ButtonHolderData(
            id = 0,
            name = "Buttons"
        )
    )
}