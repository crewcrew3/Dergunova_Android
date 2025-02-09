package ru.itis.application6.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.application6.data.dao.SongDao
import ru.itis.application6.data.dao.UserAndSongsDao
import ru.itis.application6.data.dao.UserDao
import ru.itis.application6.data.entities.SongEntity
import ru.itis.application6.data.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SongEntity::class
    ],
    version = 1
)
abstract class SongDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val songDao: SongDao
    abstract val userAndSongDao: UserAndSongsDao
}