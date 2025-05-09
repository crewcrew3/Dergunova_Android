package ru.itis.application7.core.domain.model

data class MainDataModel(
    val temp: Float,
    val feelsLike: Float,
    val pressure: Int,
    val humidity: Int,
) {
    companion object {
        val EMPTY = MainDataModel(
            temp = -100f,
            feelsLike = -100f,
            pressure = -1,
            humidity = -1,
        )
    }
}
