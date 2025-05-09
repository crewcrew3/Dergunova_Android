package ru.itis.application7.core.domain.model

import ru.itis.application7.core.utils.OtherProperties

data class CurrentWeatherModel(
    val cityName: String,
    val weather: List<WeatherDataModel>,
    val main: MainDataModel,
    val wind: WindDataModel,
) {
    companion object {
        val EMPTY = CurrentWeatherModel(
            cityName = OtherProperties.EMPTY_CITY_NAME,
            weather = listOf(WeatherDataModel.EMPTY),
            main = MainDataModel.EMPTY,
            wind = WindDataModel.EMPTY,
        )
    }
}
