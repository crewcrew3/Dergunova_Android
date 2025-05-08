package ru.itis.application7.core.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.itis.application7.core.network.pojo.response.CurrentWeatherResponse

interface OpenWeatherApi {

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
    ) : CurrentWeatherResponse?
}