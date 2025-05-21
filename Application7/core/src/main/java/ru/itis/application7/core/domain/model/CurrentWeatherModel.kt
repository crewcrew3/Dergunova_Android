package ru.itis.application7.core.domain.model

import androidx.compose.runtime.Immutable
import ru.itis.application7.core.utils.OtherProperties

@Immutable
data class CurrentWeatherModel(
    val cityName: String,
    val weather: List<WeatherDataModel>, //по логике этот список не будет меняться в рамках одного экземпляра
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
