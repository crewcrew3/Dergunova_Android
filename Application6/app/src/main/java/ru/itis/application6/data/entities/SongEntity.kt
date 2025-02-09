package ru.itis.application6.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
        )
    ]
)
data class SongEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_id")
    var songId: Int = 0,

    @ColumnInfo(name = "user_id")
    var userId: Int,

    @ColumnInfo(name = "song_name")
    var songName: String,

    @ColumnInfo(name = "song_author")
    var songAuthor: String?,

    @ColumnInfo(name = "song_year")
    var songYear: Int?,

    @ColumnInfo(name = "song_lyrics")
    var songLyrics: String?,

    @ColumnInfo(name = "song_poster_url")
    var songPosterUrl: String?,
)

