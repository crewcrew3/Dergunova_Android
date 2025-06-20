package ru.itis.application7.registration.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.application7.core.domain.exception.DuplicateNicknameException
import ru.itis.application7.core.domain.exception.IncorrectUserNicknameInputException
import ru.itis.application7.core.domain.exception.IncorrectUserPasswordInputException
import ru.itis.application7.core.domain.exception.UnknownEventException
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.usecase.GetUserByNicknameUseCase
import ru.itis.application7.core.domain.usecase.RegisterUserUseCase
import ru.itis.application7.core.utils.properties.CrashlyticsKeys
import ru.itis.application7.core.utils.properties.ExceptionsMessages
import ru.itis.application7.core.utils.properties.OtherProperties
import ru.itis.application7.registration.state.RegistrationScreenEvent
import ru.itis.application7.registration.state.RegistrationScreenState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getUserByNicknameUseCase: GetUserByNicknameUseCase,
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
                val userId = getUserByNicknameUseCase(nickname).userId.toString()
                Firebase.crashlytics.setCustomKeys {
                    key(CrashlyticsKeys.CRASHLYTICS_USER_ID, userId)
                }
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