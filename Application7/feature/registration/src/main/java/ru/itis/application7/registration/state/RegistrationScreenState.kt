package ru.itis.application7.registration.state

sealed interface RegistrationScreenState {
    data object Initial : RegistrationScreenState
    data object OnRegistrationSuccess : RegistrationScreenState
    data object ErrorNickInput: RegistrationScreenState
    data object ErrorPassInput: RegistrationScreenState
    data class Error(val message: String?) : RegistrationScreenState
}