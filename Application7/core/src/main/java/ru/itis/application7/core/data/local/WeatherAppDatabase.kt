package ru.itis.application7.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.itis.application7.core.data.local.dao.UserDao
import ru.itis.application7.core.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1
)
abstract class WeatherAppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}