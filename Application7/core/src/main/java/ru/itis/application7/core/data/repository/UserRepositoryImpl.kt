package ru.itis.application7.core.data.repository

import ru.itis.application7.core.data.local.dao.UserDao
import ru.itis.application7.core.data.local.mapper.UserMapper
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userMapper: UserMapper,
) : UserRepository {

    override suspend fun registerUser(user: UserModel) {
        userDao.saveUser(userMapper.mapToEntity(user))
    }

    override suspend fun getUserByNickname(nickname: String): UserModel {
        return userDao.getUserByNickname(nickname).let(userMapper::mapToModel)
    }
}