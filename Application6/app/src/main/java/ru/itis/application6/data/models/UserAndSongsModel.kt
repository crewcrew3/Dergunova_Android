package ru.itis.application6.data.models

import androidx.room.Embedded
import androidx.room.Relation
import ru.itis.application6.data.entities.SongEntity
import ru.itis.application6.data.entities.UserEntity

data class UserAndSongsModel(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id",
    )
    val songs: List<SongEntity>,
)
