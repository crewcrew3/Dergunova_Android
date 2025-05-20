package ru.itis.application7.core.feature.listcontent.cache

import ru.itis.application7.core.domain.model.CurrentWeatherModel
import java.time.Instant

class WeatherCache(
    private val cooldown: Long
) {
    private val cache = mutableMapOf<String, CachedWeatherData>()

    fun get(cityName: String): CachedWeatherData? {
        return cache[cityName]
    }

    fun put(cityName: String, payload: CurrentWeatherModel) {
        cache[cityName] = CachedWeatherData(payload, Instant.now(), 0)
    }

    fun refreshCache(cityName: String, payload: CurrentWeatherModel) {
        cache[cityName]?.let {
            cache[cityName] = CachedWeatherData(payload, Instant.now(), 0)
        }
    }

    fun incrementRequestCount(citiesNames: List<String>) {
        cache.forEach { entry ->
            if (entry.key !in citiesNames) {
                entry.value.intermediateRequestCount++
            }
        }
    }

    fun isCacheExist(cityName: String): Boolean {
        return cache[cityName] != null
    }

    fun isCacheValid(cityName: String): Boolean {
        val cachedResult = cache[cityName] ?: return false
        val elapsedTime = Instant.now().epochSecond - cachedResult.lastRequestTime.epochSecond
        return elapsedTime < cooldown && cachedResult.intermediateRequestCount < 3
    }

    override fun toString(): String {
        val list = mutableListOf<String>()
        cache.forEach { (k, v) ->
            list.add(k + ": запросов - " + v.intermediateRequestCount + ", время прошло - " + (Instant.now().epochSecond - v.lastRequestTime.epochSecond))
        }
        return list.toString()
    }
}

data class CachedWeatherData(
    val payload: CurrentWeatherModel,
    val lastRequestTime: Instant,
    var intermediateRequestCount: Int // Количество промежуточных запросов
)
