package ru.itis.application7.core.network.pojo.response

import com.google.gson.annotations.SerializedName

class WindData(
    @SerializedName("speed")
    val speed: Float?,
)
