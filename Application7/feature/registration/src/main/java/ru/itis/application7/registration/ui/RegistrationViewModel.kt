package ru.itis.application7.registration.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.itis.application7.core.domain.exception.DuplicateNicknameException
import ru.itis.application7.core.domain.exception.IncorrectUserNicknameInputException
import ru.itis.application7.core.domain.exception.IncorrectUserPasswordInputException
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.usecase.RegisterUserUseCase
import ru.itis.application7.core.utils.ExceptionsMessages
import ru.itis.application7.core.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val sharedPref: SharedPreferences,
) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<String?>()
    val errorFlow: SharedFlow<String?> = _errorFlow.asSharedFlow()

    private val _errorNickInputFlow = MutableSharedFlow<Boolean>()
    val errorNickInputFlow: SharedFlow<Boolean> = _errorNickInputFlow.asSharedFlow()

    private val _errorPassInputFlow = MutableSharedFlow<Boolean>()
    val errorPassInputFlow: SharedFlow<Boolean> = _errorPassInputFlow.asSharedFlow()

    private val _registerSuccessFlow = MutableSharedFlow<Boolean>()
    val registerSuccessFlow: SharedFlow<Boolean> = _registerSuccessFlow.asSharedFlow()

    fun registerUser(nickname: String, password: String) {
        viewModelScope.launch {
            runCatching {
                registerUserUseCase(
                    UserModel(
                        userNickname = nickname,
                        userPassword = password,
                    )
                )
            }.onSuccess {
                sharedPref.edit().putString(OtherProperties.USER_NICK_SHARED_PREF, nickname).apply()
                _registerSuccessFlow.emit(true)
            }.onFailure { exception ->
                _registerSuccessFlow.emit(false)
                when (exception) {
                    is IncorrectUserNicknameInputException -> _errorNickInputFlow.emit(true)
                    is IncorrectUserPasswordInputException -> _errorPassInputFlow.emit(true)
                    is DuplicateNicknameException -> _errorFlow.emit(exception.message)
                    else -> _errorFlow.emit(exception.message ?: ExceptionsMessages.UNKNOWN_ERROR)
                }
            }
        }
    }
}