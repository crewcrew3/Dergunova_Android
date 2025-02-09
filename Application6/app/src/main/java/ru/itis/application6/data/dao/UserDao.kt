package ru.itis.application6.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.application6.data.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE user_nickname = :nickname")
    suspend fun getUserByNickname(nickname: String) : UserEntity?

    @Query("SELECT user_id FROM users WHERE user_nickname = :nickname")
    suspend fun getUserIDByNickname(nickname: String) : Int?
}