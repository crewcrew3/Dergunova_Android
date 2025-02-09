package ru.itis.application6.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application6.data.dao.UserAndSongsDao
import ru.itis.application6.data.models.UserAndSongsModel

class UserAndSongsRepository(
    private val userAndSongsDao: UserAndSongsDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getUserAndTheirsSongs(nickname: String): UserAndSongsModel? {
        return withContext(ioDispatcher) {
            userAndSongsDao.getUserAndTheirsSongs(nickname)
        }
    }

    suspend fun deleteSong(userID: Int, songID: Int) {
        return withContext(ioDispatcher) {
            userAndSongsDao.deleteSong(userID, songID)
        }
    }
}