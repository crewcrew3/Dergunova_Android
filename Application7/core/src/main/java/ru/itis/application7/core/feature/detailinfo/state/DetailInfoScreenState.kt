package ru.itis.application7.core.feature.detailinfo.state

import ru.itis.application7.core.domain.model.CurrentWeatherModel

sealed interface DetailInfoScreenState {
    data object Initial : DetailInfoScreenState
    data object Loading : DetailInfoScreenState
    data class Result(val result: CurrentWeatherModel) : DetailInfoScreenState
    data class Error(val message: String?) : DetailInfoScreenState
    data class ErrorHttp(val message: List<String?>) : DetailInfoScreenState
}