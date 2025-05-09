package ru.itis.application7.core.data.local.mapper

import ru.itis.application7.core.data.local.entity.UserEntity
import ru.itis.application7.core.domain.model.UserModel
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun mapToModel(input: UserEntity?): UserModel {
        return input?.let { user ->
            UserModel(
                userId = user.userId,
                userNickname = user.userNickname,
                userPassword = user.userPassword,
            )
        } ?: UserModel.EMPTY
    }

    fun mapToEntity(input: UserModel): UserEntity {
        return UserEntity(
            userNickname = input.userNickname,
            userPassword = input.userPassword,
        )
    }
}