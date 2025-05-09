package ru.itis.application7.registration.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.application7.core.domain.exception.DuplicateNicknameException
import ru.itis.application7.core.domain.exception.IncorrectUserNicknameInputException
import ru.itis.application7.core.domain.exception.IncorrectUserPasswordInputException
import ru.itis.application7.core.domain.exception.UnknownEventException
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.usecase.RegisterUserUseCase
import ru.itis.application7.core.utils.ExceptionsMessages
import ru.itis.application7.core.utils.OtherProperties
import ru.itis.application7.registration.state.RegistrationScreenEvent
import ru.itis.application7.registration.state.RegistrationScreenState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val sharedPref: SharedPreferences,
) : ViewModel() {

    private val _pageState = MutableStateFlow<RegistrationScreenState>(value = RegistrationScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    fun reduce(event: RegistrationScreenEvent) {
        when (event) {
            is RegistrationScreenEvent.OnSignUp -> registerUser(nickname = event.nickname, password = event.password)
            else -> throw UnknownEventException(ExceptionsMessages.UNKNOWN_EVENT)
        }
    }

    private fun registerUser(nickname: String, password: String) {
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
                _pageState.value = RegistrationScreenState.OnRegistrationSuccess
            }.onFailure { exception ->
                when (exception) {
                    is IncorrectUserNicknameInputException -> _pageState.value = RegistrationScreenState.ErrorNickInput
                    is IncorrectUserPasswordInputException -> _pageState.value = RegistrationScreenState.ErrorPassInput
                    is DuplicateNicknameException -> _pageState.value = RegistrationScreenState.Error(exception.message)
                    else -> _pageState.value = RegistrationScreenState.Error(exception.message ?: ExceptionsMessages.UNKNOWN_ERROR)
                }
            }
        }
    }
}