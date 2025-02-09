package ru.itis.application6.services

import ru.itis.application6.data.models.UserAndSongsModel
import ru.itis.application6.data.repository.UserAndSongsRepository

class UserAndSongsService(
    private val userAndSongsRepository: UserAndSongsRepository
) {

    suspend fun getUserAndTheirsSongs(nickname: String): UserAndSongsModel? {
        return userAndSongsRepository.getUserAndTheirsSongs(nickname)
    }

    suspend fun deleteSong(userID: Int, songID: Int) {
        return userAndSongsRepository.deleteSong(userID, songID)
    }

}