package ru.itis.application7.core.domain.model

import ru.itis.application7.core.utils.OtherProperties

data class UserModel(
    val userId: Int = 0,
    val userNickname: String,
    val userPassword: String,
) {
    companion object {
        val EMPTY = UserModel(
            userId = -1,
            userNickname = OtherProperties.EMPTY_USER_NAME,
            userPassword = OtherProperties.EMPTY_USER_PASS,
        )
    }
}