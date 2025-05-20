package ru.itis.application7.core.domain.repository

import ru.itis.application7.core.domain.model.UserModel

interface UserRepository {
    suspend fun registerUser(user: UserModel)
    suspend fun getUserByNickname(nickname: String): UserModel
}