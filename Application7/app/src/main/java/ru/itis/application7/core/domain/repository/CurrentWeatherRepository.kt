package ru.itis.application7.core.domain.repository

import ru.itis.application7.core.domain.model.CurrentWeatherModel

interface CurrentWeatherRepository {
    suspend fun getWeatherByCityName(cityName: String): CurrentWeatherModel
}