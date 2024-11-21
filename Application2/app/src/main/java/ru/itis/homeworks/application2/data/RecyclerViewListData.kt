package ru.itis.homeworks.application2.data

import ru.itis.homeworks.application2.recycler_view.ButtonHolderData
import ru.itis.homeworks.application2.recycler_view.MultipleHolderData
import ru.itis.homeworks.application2.recycler_view.SongHolderData

object RecyclerViewListData {

    val songs: MutableList<MultipleHolderData> = mutableListOf(
        ButtonHolderData(
            id = 0,
            name = "Buttons"
        ),
        SongHolderData (
            id = 1,
            name = "Sample Song",
            lyrics = "Lalala lala laaaaaa",
            url = "https://avatars.mds.yandex.net/i?id=e6bdce08ed45888e9994b7b740a70b6ba8fd246a-12719024-images-thumbs&n=13"
        )
    )
}