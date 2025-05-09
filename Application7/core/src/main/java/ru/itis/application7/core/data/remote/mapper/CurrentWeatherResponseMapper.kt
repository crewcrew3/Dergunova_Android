package ru.itis.application7.core.data.remote.mapper

import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.network.pojo.response.CurrentWeatherResponse
import ru.itis.application7.core.utils.OtherProperties
import javax.inject.Inject

class CurrentWeatherResponseMapper @Inject constructor(
    private val weatherDataMapper: WeatherDataMapper,
    private val mainDataMapper: MainDataMapper,
    private val windDataMapper: WindDataMapper,
) {
    fun map(input: CurrentWeatherResponse?): CurrentWeatherModel {
        return input?.let { response ->
            CurrentWeatherModel(
                cityName = response.cityName ?: OtherProperties.EMPTY_CITY_NAME,
                weather = weatherDataMapper.map(response.weather),
                main = mainDataMapper.map(response.main),
                wind = windDataMapper.map(response.wind)
            )
        } ?: CurrentWeatherModel.EMPTY
    }
}