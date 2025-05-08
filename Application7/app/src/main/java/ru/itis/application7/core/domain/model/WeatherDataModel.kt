package ru.itis.application7.core.domain.model

import ru.itis.application7.core.utils.OtherProperties

data class WeatherDataModel(
    val main: String,
    val description: String,
) {
    companion object {
        val EMPTY = WeatherDataModel(
            main = OtherProperties.EMPTY_WEATHER,
            description = OtherProperties.EMPTY_WEATHER_DESC,
        )
    }
}
