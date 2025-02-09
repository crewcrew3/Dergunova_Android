package ru.itis.application6.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application6.data.dao.UserDao
import ru.itis.application6.data.entities.UserEntity

class UserRepository(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun saveUser(user: UserEntity) {
        return withContext(ioDispatcher) {
            userDao.saveUser(user = user)
        }
    }

    suspend fun getUserByNickname(nickname: String) : UserEntity? {
        return withContext(ioDispatcher) {
            userDao.getUserByNickname(nickname)
        }
    }

    suspend fun getUserIDByNickname(nickname: String) : Int? {
        return withContext(ioDispatcher) {
            userDao.getUserIDByNickname(nickname)
        }
    }
}