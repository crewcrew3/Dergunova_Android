package ru.itis.application7.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application7.core.domain.di.qualifiers.IoDispatchers
import ru.itis.application7.core.domain.exception.DuplicateNicknameException
import ru.itis.application7.core.domain.exception.IncorrectUserNicknameInputException
import ru.itis.application7.core.domain.exception.IncorrectUserPasswordInputException
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.repository.UserRepository
import ru.itis.application7.core.utils.ExceptionsMessages
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatchers val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(user: UserModel) {
        withContext(dispatcher) {
            if (userRepository.getUserByNickname(user.userNickname) != UserModel.EMPTY) {
                throw DuplicateNicknameException(ExceptionsMessages.DUPLICATE_NICKNAME)
            }

            if (user.userNickname.isBlank()) {
                throw IncorrectUserNicknameInputException(ExceptionsMessages.INCORRECT_USER_NICKNAME_INPUT)
            }

            if (user.userPassword.isBlank()) {
                throw IncorrectUserPasswordInputException(ExceptionsMessages.INCORRECT_USER_PASS_INPUT)
            }

            userRepository.registerUser(user)
        }
    }
}