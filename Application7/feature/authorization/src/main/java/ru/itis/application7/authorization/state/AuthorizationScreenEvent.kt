package ru.itis.application7.authorization.state

sealed interface AuthorizationScreenEvent {
    data class OnLogIn(val nickname: String, val password: String) : AuthorizationScreenEvent
}