package ru.itis.application7.core.network.pojo.response

import com.google.gson.annotations.SerializedName

class MainData(

    @SerializedName("temp")
    val temp: Float?,

    @SerializedName("feels_like")
    val feelsLike: Float?,

    @SerializedName("pressure")
    val pressure: Int?,

    @SerializedName("humidity")
    val humidity: Int?,
)
