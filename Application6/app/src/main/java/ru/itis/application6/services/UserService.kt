package ru.itis.application6.services

import ru.itis.application6.data.entities.UserEntity
import ru.itis.application6.data.repository.UserRepository
import ru.itis.application6.utils.PasswordHasher
import ru.itis.application6.utils.ValidationHelper

class UserService(
    private val userRepository: UserRepository
) {

    suspend fun saveUser(nickname: String, password: String, age: String?, aboutYourself: String?) : Boolean {
        if (ValidationHelper.validateUserData(nickname, password, age)) {
            val hashedPassAndSalt = PasswordHasher.hashPassWithSalt(password)
            userRepository.saveUser(
                UserEntity(
                    userNickname = nickname,
                    userPassword = hashedPassAndSalt[0],
                    userPasswordSalt = hashedPassAndSalt[1],
                    userAge = age?.toInt(),
                    userAboutYourself = aboutYourself,
                )
            )
            return true
        }
        return false
    }

    suspend fun isUserExists(nickname: String) : Boolean {
        var user: UserEntity? = null
        if (nickname.isNotBlank()) {
            user = userRepository.getUserByNickname(nickname)
        }
        return user != null
    }

    suspend fun verifyUser(nickname: String, password: String) : Boolean {
        if (ValidationHelper.validateUserData(nickname, password, null)) {
            val user = userRepository.getUserByNickname(nickname)
            user?.run {
                if (PasswordHasher.verifyPassword(password, userPassword, userPasswordSalt)) {
                    return true
                }
            }
        }
        return false
    }

    suspend fun getUserIDByNickname(nickname: String?) : Int? {
        nickname?.let { safeNickname ->
            return userRepository.getUserIDByNickname(safeNickname)
        }
        return null
    }
}