package ru.itis.application7.core.feature.detailinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.itis.application7.core.domain.exception.CombinedWeatherException
import ru.itis.application7.core.domain.exception.UnknownEventException
import ru.itis.application7.core.domain.usecase.GetCurrentWeatherByCityNameUseCase
import ru.itis.application7.core.feature.detailinfo.state.DetailInfoScreenEvent
import ru.itis.application7.core.feature.detailinfo.state.DetailInfoScreenState
import ru.itis.application7.core.utils.properties.ExceptionsMessages
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val getCurrentWeatherByCityNameUseCase: GetCurrentWeatherByCityNameUseCase,
) : ViewModel() {

    private val _pageState = MutableStateFlow<DetailInfoScreenState>(value = DetailInfoScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    var numberOfLoadingRowItems = 3 //я не знаю как по факту посчитать сколько будет элементов в списке погоды, поэтому по умолчанию буду отображать 3

    fun reduce(event: DetailInfoScreenEvent) {
        when (event) {
            is DetailInfoScreenEvent.OnAlertDialogClosed -> _pageState.value = DetailInfoScreenState.Initial
            is DetailInfoScreenEvent.OnScreenInit -> getCurrentWeatherByCityName(event.query)
            else -> throw UnknownEventException(ExceptionsMessages.UNKNOWN_EVENT)
        }
    }

    private fun getCurrentWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            _pageState.value = DetailInfoScreenState.Loading
            runCatching {
                getCurrentWeatherByCityNameUseCase(cityName)
            }.onSuccess { weatherModel ->
                _pageState.value = DetailInfoScreenState.Result(result = weatherModel)
            }.onFailure { exception ->
                when (exception) {
                    is CombinedWeatherException -> {
                        _pageState.value = DetailInfoScreenState.Error(message = exception.message)
                    }
                    is HttpException -> {
                        _pageState.value =
                            DetailInfoScreenState.ErrorHttp(
                                message = listOf(
                                    exception.code().toString(),
                                    exception.message
                                )
                            )
                    }
                    else -> {
                        _pageState.value = DetailInfoScreenState.Error(message = exception.message ?: ExceptionsMessages.UNKNOWN_ERROR)
                    }
                }
            }
        }
    }
}