package ru.itis.application7.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application7.core.domain.di.qualifiers.IoDispatchers
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatchers val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(nickname: String): UserModel {
        return withContext(dispatcher) {
            userRepository.getUserByNickname(nickname)
        }
    }
}