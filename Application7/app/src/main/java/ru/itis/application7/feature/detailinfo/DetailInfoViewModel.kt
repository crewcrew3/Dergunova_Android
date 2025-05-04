package ru.itis.application7.feature.detailinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.application7.core.domain.exception.CombinedWeatherException
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.usecase.GetCurrentWeatherByCityNameUseCase
import ru.itis.application7.core.utils.ExceptionsMessages
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val getCurrentWeatherByCityNameUseCase: GetCurrentWeatherByCityNameUseCase,
) : ViewModel() {

    private val _weatherInfoFlow = MutableStateFlow(CurrentWeatherModel.EMPTY)
    val weatherInfoFlow = _weatherInfoFlow.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String?>()
    val errorFlow: SharedFlow<String?> = _errorFlow.asSharedFlow()

    private val _errorHttpFlow = MutableSharedFlow<List<String?>>()
    val errorHttpFlow: SharedFlow<List<String?>> = _errorHttpFlow.asSharedFlow()

    fun getCurrentWeatherByCityName(cityName: String) {
        viewModelScope.launch {
            runCatching {
                getCurrentWeatherByCityNameUseCase(cityName)
            }.onSuccess { weatherModel ->
                _weatherInfoFlow.value = weatherModel
            }.onFailure { exception ->
                when (exception) {
                    is CombinedWeatherException -> {
                        _errorFlow.emit(exception.message)
                    }
                    else -> {
                        _errorFlow.emit(exception.message ?: ExceptionsMessages.UNKNOWN_ERROR)
                    }
                }
            }
        }
    }
}