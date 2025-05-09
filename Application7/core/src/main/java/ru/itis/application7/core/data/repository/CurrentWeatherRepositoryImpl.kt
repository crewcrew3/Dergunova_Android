package ru.itis.application7.core.data.repository

import ru.itis.application7.core.data.remote.mapper.CurrentWeatherResponseMapper
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.repository.CurrentWeatherRepository
import ru.itis.application7.core.network.OpenWeatherApi
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val currentWeatherResponseMapper: CurrentWeatherResponseMapper,
): CurrentWeatherRepository {
    override suspend fun getWeatherByCityName(cityName: String): CurrentWeatherModel {
        return openWeatherApi.getWeatherByCityName(cityName).let(currentWeatherResponseMapper::map)
    }
}