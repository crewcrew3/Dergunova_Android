package ru.itis.application7.core.network.pojo.response

import com.google.gson.annotations.SerializedName

class WeatherData(

    @SerializedName("main")
    val main: String?,

    @SerializedName("description")
    val description: String?,
)
