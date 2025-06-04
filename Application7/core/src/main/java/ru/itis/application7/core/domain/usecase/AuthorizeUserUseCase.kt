package ru.itis.application7.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application7.core.domain.di.qualifiers.IoDispatchers
import ru.itis.application7.core.domain.exception.UnregisteredUserException
import ru.itis.application7.core.domain.exception.WrongPasswordException
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.repository.UserRepository
import ru.itis.application7.core.utils.properties.ExceptionsMessages
import javax.inject.Inject

class AuthorizeUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatchers val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(user: UserModel) {
        withContext(dispatcher) {
            val foundUser = userRepository.getUserByNickname(user.userNickname)
            if (foundUser == UserModel.EMPTY) {
                throw UnregisteredUserException(ExceptionsMessages.UNREGISTERED_USER)
            }

            if (user.userPassword != foundUser.userPassword) {
                throw WrongPasswordException(ExceptionsMessages.WRONG_PASSWORD)
            }
        }
    }
}