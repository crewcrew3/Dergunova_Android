package ru.itis.application7.registration.state

sealed interface RegistrationScreenEvent {
    data class OnSignUp(val nickname: String, val password: String) : RegistrationScreenEvent
}