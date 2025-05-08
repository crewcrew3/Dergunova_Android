package ru.itis.application7.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListContentRoute

@Serializable
data class DetailInfoRoute(val cityName: String)