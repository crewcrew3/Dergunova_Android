package ru.itis.application7.core.domain.model

data class WeatherDataModel(
    val main: String,
    val description: String,
) {
    companion object {
        val EMPTY = WeatherDataModel(
            main = "",
            description = "",
        )
    }
}
