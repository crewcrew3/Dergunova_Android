package ru.itis.application7.core.feature.listcontent

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.itis.application7.core.domain.exception.CombinedWeatherException
import ru.itis.application7.core.domain.exception.UnknownEventException
import ru.itis.application7.core.domain.usecase.GetListCurrentWeatherByListCitiesNamesUseCase
import ru.itis.application7.core.feature.listcontent.state.ListContentScreenEvent
import ru.itis.application7.core.feature.listcontent.state.ListContentScreenState
import ru.itis.application7.core.utils.ExceptionsMessages
import ru.itis.application7.core.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
class ListContentViewModel @Inject constructor(
    private val getListCurrentWeatherByListCitiesNamesUseCase: GetListCurrentWeatherByListCitiesNamesUseCase,
    private val sharedPref: SharedPreferences,
) : ViewModel() {

    private val _pageState = MutableStateFlow<ListContentScreenState>(value = ListContentScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    var numberOfLoadingItems = 0

    fun reduce(event: ListContentScreenEvent) {
        when (event) {
            is ListContentScreenEvent.OnSearchQueryChanged -> getCurrentWeatherByCitiesNames(event.query)
            is ListContentScreenEvent.OnLogOut -> logOutUser()
            is ListContentScreenEvent.OnAlertDialogClosed -> _pageState.value = ListContentScreenState.Initial
            else -> throw UnknownEventException(ExceptionsMessages.UNKNOWN_EVENT)
        }
    }

    private fun getCurrentWeatherByCitiesNames(citiesNamesStr: String) {
        viewModelScope.launch {
            _pageState.value = ListContentScreenState.Loading
            val regex = Regex(REGEX_FOR_CITIES_NAMES)
            if (!regex.matches(citiesNamesStr)) {
                _pageState.value = ListContentScreenState.ErrorInput
            } else {
                val citiesNames = citiesNamesStr
                    .replace(Regex("\\s+"), " ") //если в слове 1 и более пробел, заменяем на 1 пробел
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                numberOfLoadingItems = citiesNames.size

                runCatching {
                    getListCurrentWeatherByListCitiesNamesUseCase(citiesNames)
                }.onSuccess { listWeatherModel ->
                    _pageState.value = ListContentScreenState.SearchResult(result = listWeatherModel)
                }.onFailure { exception ->
                    when (exception) {
                        is CombinedWeatherException -> {
                            _pageState.value = ListContentScreenState.Error(message = exception.message)
                        }
                        is HttpException -> {
                            _pageState.value =
                                ListContentScreenState.ErrorHttp(
                                    message = listOf(
                                        exception.code().toString(),
                                        exception.message
                                    )
                                )
                        }
                        else -> {
                            _pageState.value = ListContentScreenState.Error(message = exception.message ?: ExceptionsMessages.UNKNOWN_ERROR)
                        }
                    }
                }.also { //все сбрасываем в конце: и в случае успеха, и в случае ошибки
                    numberOfLoadingItems = 0
                }
            }
        }
    }

    private fun logOutUser() {
        sharedPref.edit().remove(OtherProperties.USER_NICK_SHARED_PREF).apply()
    }

    private companion object {
        const val REGEX_FOR_CITIES_NAMES = "^\\s*([a-zA-Zа-яА-Я.](\\s+[a-zA-Zа-яА-Я.]+)?)+(\\s*,\\s*([a-zA-Zа-яА-Я.](\\s+[a-zA-Zа-яА-Я.]+)?)+)*\\s*$"
    }
}