package ru.itis.application7.core.domain.model

data class CurrentWeatherModel(
    val cityName: String,
    val weather: List<WeatherDataModel>,
    val main: MainDataModel,
    val wind: WindDataModel,
) {
    companion object {
        val EMPTY = CurrentWeatherModel(
            cityName = "",
            weather = emptyList(),
            main = MainDataModel.EMPTY,
            wind = WindDataModel.EMPTY,
        )
    }
}
