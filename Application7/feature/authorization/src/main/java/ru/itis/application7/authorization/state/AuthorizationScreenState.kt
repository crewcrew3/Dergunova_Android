package ru.itis.application7.authorization.state

sealed interface AuthorizationScreenState {
    data object Initial : AuthorizationScreenState
    data object OnAuthSuccess : AuthorizationScreenState
    data class Error(val message: String?) : AuthorizationScreenState
}