package ru.itis.application6.data.dao

import androidx.room.Dao
import androidx.room.Query
import ru.itis.application6.data.models.UserAndSongsModel

@Dao
interface UserAndSongsDao {

    @Query("SELECT * FROM users WHERE user_nickname = :nickname")
    fun getUserAndTheirsSongs(nickname: String): UserAndSongsModel?

    @Query("DELETE FROM songs WHERE song_id = :songID AND user_id = :userID")
    fun deleteSong(userID: Int, songID: Int)
}