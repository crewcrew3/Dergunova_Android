package ru.itis.application7.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.application7.core.di.qualifiers.IoDispatchers
import ru.itis.application7.core.domain.exception.CombinedWeatherException
import ru.itis.application7.core.domain.exception.EmptyCurrentWeatherException
import ru.itis.application7.core.domain.exception.EmptyFeelsLikeException
import ru.itis.application7.core.domain.exception.EmptyHumidityException
import ru.itis.application7.core.domain.exception.EmptyPressureException
import ru.itis.application7.core.domain.exception.EmptyTemperatureException
import ru.itis.application7.core.domain.exception.EmptyWeatherException
import ru.itis.application7.core.domain.exception.EmptyWindSpeedException
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.repository.CurrentWeatherRepository
import ru.itis.application7.core.utils.ExceptionsMessages
import javax.inject.Inject

class GetCurrentWeatherByCityNameUseCase @Inject constructor(
    private val weatherRepository: CurrentWeatherRepository,
    @IoDispatchers val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(cityName: String): CurrentWeatherModel {
        return withContext(dispatcher) {
            val weather = weatherRepository.getWeatherByCityName(cityName)

            val errors = mutableListOf<Throwable>()
            if (weather.cityName.isBlank()) {
                errors.add(EmptyCurrentWeatherException(ExceptionsMessages.EMPTY_CURRENT_WEATHER))
            }
            if (weather.weather.isEmpty() || weather.weather.any { item -> item.main.isBlank() || item.description.isBlank() }) {
                errors.add(EmptyWeatherException(ExceptionsMessages.EMPTY_WEATHER))
            }
            if (weather.main.temp == -100f) {
                errors.add(EmptyTemperatureException(ExceptionsMessages.EMPTY_TEMP))
            }
            if (weather.main.feelsLike == -100f) {
                errors.add(EmptyFeelsLikeException(ExceptionsMessages.EMPTY_FEELS_LIKE))
            }
            if (weather.main.pressure == -1) {
                errors.add(EmptyPressureException(ExceptionsMessages.EMPTY_PRESSURE))
            }
            if (weather.main.humidity == -1) {
                errors.add(EmptyHumidityException(ExceptionsMessages.EMPTY_HUMIDITY))
            }
            if (weather.wind.speed == -1f) {
                errors.add(EmptyWindSpeedException(ExceptionsMessages.EMPTY_WIND_SPEED))
            }

            if (errors.isNotEmpty()) {
                throw CombinedWeatherException(errors)
            }

            weather
        }
    }
}