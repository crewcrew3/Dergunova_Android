package ru.itis.application7.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListContentRoute

@Serializable
data class DetailInfoRoute(val cityName: String)

@Serializable
object RegistrationRoute

@Serializable
object AuthorizationRoute

@Serializable
object GraphRoute