package ru.itis.application7.feature.listcontent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.itis.application7.core.domain.exception.CombinedWeatherException
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.usecase.GetListCurrentWeatherByListCitiesNamesUseCase
import ru.itis.application7.core.utils.ExceptionsMessages
import javax.inject.Inject

@HiltViewModel
class ListContentViewModel @Inject constructor(
    private val getListCurrentWeatherByListCitiesNamesUseCase: GetListCurrentWeatherByListCitiesNamesUseCase,
) : ViewModel() {

    private val _weatherInfoFlow = MutableStateFlow<List<CurrentWeatherModel>>(emptyList())
    val weatherInfoFlow = _weatherInfoFlow.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String?>()
    val errorFlow: SharedFlow<String?> = _errorFlow.asSharedFlow()

    private val _errorInputFlow = MutableSharedFlow<Boolean>()
    val errorInputFlow: SharedFlow<Boolean> = _errorInputFlow.asSharedFlow()

    private val _errorHttpFlow = MutableSharedFlow<List<String?>>()
    val errorHttpFlow: SharedFlow<List<String?>> = _errorHttpFlow.asSharedFlow()

    private val _isContentLoadingFlow = MutableStateFlow(false)
    val isContentLoadingFlow = _isContentLoadingFlow.asStateFlow()

    var numberOfLoadingItems = 0

    fun getCurrentWeatherByCitiesNames(citiesNamesStr: String) {
        viewModelScope.launch {
            _isContentLoadingFlow.value = true
            val regex = Regex("^\\s*([a-zA-Zа-яА-Я.](\\s+[a-zA-Zа-яА-Я.]+)?)+(\\s*,\\s*([a-zA-Zа-яА-Я.](\\s+[a-zA-Zа-яА-Я.]+)?)+)*\\s*$")
            if (!regex.matches(citiesNamesStr)) {
                _errorInputFlow.emit(true)
                _isContentLoadingFlow.value = false
            } else {
                _errorInputFlow.emit(false)
                val citiesNames = citiesNamesStr
                    .replace(Regex("\\s+"), " ") //если в слове 1 и более пробел, заменяем на 1 пробел
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                numberOfLoadingItems = citiesNames.size

                runCatching {
                    getListCurrentWeatherByListCitiesNamesUseCase(citiesNames)
                }.onSuccess { listWeatherModel ->
                    _weatherInfoFlow.value = listWeatherModel
                }.onFailure { exception ->
                    when (exception) {
                        is CombinedWeatherException -> {
                            _errorFlow.emit(exception.message)
                        }
                        is HttpException -> {
                            _errorHttpFlow.emit(
                                listOf(
                                    exception.code().toString(),
                                    exception.message
                                )
                            )
                        }
                        else -> {
                            _errorFlow.emit(exception.message ?: ExceptionsMessages.UNKNOWN_ERROR)
                        }
                    }
                }.also { //все сбрасываем в конце: и в случае успеха, и в случае ошибки
                    numberOfLoadingItems = 0
                    _isContentLoadingFlow.value = false
                }
            }
        }
    }
}