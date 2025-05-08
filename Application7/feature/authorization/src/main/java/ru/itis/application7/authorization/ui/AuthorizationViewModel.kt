package ru.itis.application7.authorization.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.itis.application7.core.domain.exception.UnregisteredUserException
import ru.itis.application7.core.domain.exception.WrongPasswordException
import ru.itis.application7.core.domain.model.UserModel
import ru.itis.application7.core.domain.usecase.AuthorizeUserUseCase
import ru.itis.application7.core.utils.ExceptionsMessages
import ru.itis.application7.core.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel@Inject constructor(
    private val authorizeUserUseCase: AuthorizeUserUseCase,
    private val sharedPref: SharedPreferences,
) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<String?>()
    val errorFlow: SharedFlow<String?> = _errorFlow.asSharedFlow()

    private val _authSuccessFlow = MutableSharedFlow<Boolean>()
    val authSuccessFlow: SharedFlow<Boolean> = _authSuccessFlow.asSharedFlow()

    fun authUser(nickname: String, password: String) {
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
                _authSuccessFlow.emit(true)
            }.onFailure { exception ->
                _authSuccessFlow.emit(false)
                when (exception) {
                    is UnregisteredUserException -> _errorFlow.emit(exception.message)
                    is WrongPasswordException -> _errorFlow.emit(exception.message)
                    else -> _errorFlow.emit(exception.message ?: ExceptionsMessages.UNKNOWN_ERROR)
                }
            }
        }
    }
}