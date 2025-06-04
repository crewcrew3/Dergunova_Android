package ru.itis.application7.authorization.ui

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
import ru.itis.application7.authorization.state.AuthorizationScreenEvent
import ru.itis.application7.authorization.state.AuthorizationScreenState
import ru.itis.application7.core.domain.exception.UnknownEventException
import ru.itis.application7.core.domain.exception.UnregisteredUserException
import ru.itis.application7.core.domain.exception.WrongPasswordException
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.usecase.AuthorizeUserUseCase
import ru.itis.application7.core.domain.usecase.GetUserByNicknameUseCase
import ru.itis.application7.core.utils.CrashlyticsKeys
import ru.itis.application7.core.utils.ExceptionsMessages
import ru.itis.application7.core.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel@Inject constructor(
    private val authorizeUserUseCase: AuthorizeUserUseCase,
    private val getUserByNicknameUseCase: GetUserByNicknameUseCase,
    private val sharedPref: SharedPreferences,
) : ViewModel() {

    private val _pageState = MutableStateFlow<AuthorizationScreenState>(value = AuthorizationScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    fun reduce(event: AuthorizationScreenEvent) {
        when (event) {
            is AuthorizationScreenEvent.OnLogIn -> authUser(nickname = event.nickname, password = event.password)
            else -> throw UnknownEventException(ExceptionsMessages.UNKNOWN_EVENT)
        }
    }

    private fun authUser(nickname: String, password: String) {
        viewModelScope.launch {
            runCatching {
                authorizeUserUseCase(
                    UserModel(
                        userNickname = nickname,
                        userPassword = password
                    )
                )
            }.onSuccess {
                sharedPref.edit().putString(OtherProperties.USER_NICK_SHARED_PREF, nickname).apply()
                val userId = getUserByNicknameUseCase(nickname).userId.toString()
                Firebase.crashlytics.setCustomKeys {
                    key(CrashlyticsKeys.CRASHLYTICS_USER_ID, userId)
                }
                _pageState.value = AuthorizationScreenState.OnAuthSuccess
            }.onFailure { exception ->
                when (exception) {
                    is UnregisteredUserException -> { _pageState.value = AuthorizationScreenState.Error(exception.message) }
                    is WrongPasswordException ->  { _pageState.value = AuthorizationScreenState.Error(exception.message) }
                    else ->  { _pageState.value = AuthorizationScreenState.Error(exception.message ?: ExceptionsMessages.UNKNOWN_ERROR) }                }
            }
        }
    }
}