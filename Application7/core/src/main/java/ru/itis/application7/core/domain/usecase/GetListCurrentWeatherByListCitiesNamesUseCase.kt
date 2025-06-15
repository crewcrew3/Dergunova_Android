package ru.itis.application7.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application7.core.domain.di.qualifiers.IoDispatchers
import ru.itis.application7.core.domain.exception.CombinedWeatherException
import ru.itis.application7.core.domain.exception.EmptyCurrentWeatherException
import ru.itis.application7.core.domain.exception.EmptyTemperatureException
import ru.itis.application7.core.domain.exception.EmptyWeatherException
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.repository.CurrentWeatherRepository
import ru.itis.application7.core.utils.properties.ExceptionsMessages
import ru.itis.application7.core.utils.properties.OtherProperties
import javax.inject.Inject

class GetListCurrentWeatherByListCitiesNamesUseCase @Inject constructor(
    private val weatherRepository: CurrentWeatherRepository,
    @IoDispatchers val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(listCitiesNames: List<String>): List<CurrentWeatherModel> {
        return withContext(dispatcher) {
            val result = mutableListOf<CurrentWeatherModel>()
            listCitiesNames.forEach { cityName ->
                val weather = weatherRepository.getWeatherByCityName(cityName)
                checkErrors(weather)
                result.add(weather)
            }
            result
        }
    }

    private fun checkErrors(weather: CurrentWeatherModel) {
        val errors = mutableListOf<Throwable>()
        if (weather.cityName == OtherProperties.EMPTY_CITY_NAME) {
            errors.add(EmptyCurrentWeatherException(ExceptionsMessages.EMPTY_CURRENT_WEATHER))
        }
        if (weather.weather.any { item ->
                item.main == OtherProperties.EMPTY_WEATHER ||
                        item.description == OtherProperties.EMPTY_WEATHER_DESC
            }) {
            errors.add(EmptyWeatherException(ExceptionsMessages.EMPTY_WEATHER))
        }
        if (weather.main.temp == -100f) {
            errors.add(EmptyTemperatureException(ExceptionsMessages.EMPTY_TEMP))
        }
        if (errors.isNotEmpty()) {
            throw CombinedWeatherException(errors)
        }
    }
}