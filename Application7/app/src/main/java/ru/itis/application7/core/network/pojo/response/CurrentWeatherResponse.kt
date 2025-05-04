package ru.itis.application7.core.network.pojo.response

import com.google.gson.annotations.SerializedName

class CurrentWeatherResponse(

    @SerializedName("name")
    val cityName: String?,

    @SerializedName("weather")
    val weather: List<WeatherData?>?,

    @SerializedName("main")
    val main: MainData?,

    @SerializedName("wind")
    val wind: WindData?,
)
