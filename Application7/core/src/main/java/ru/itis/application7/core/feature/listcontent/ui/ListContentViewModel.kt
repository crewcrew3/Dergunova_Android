package ru.itis.application7.core.feature.listcontent.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.itis.application7.core.feature.listcontent.cache.WeatherCache
import ru.itis.application7.core.domain.exception.CombinedWeatherException
import ru.itis.application7.core.domain.exception.UnknownEventException
import ru.itis.application7.core.domain.model.CurrentWeatherModel
import ru.itis.application7.core.domain.usecase.GetListCurrentWeatherByListCitiesNamesUseCase
import ru.itis.application7.core.feature.listcontent.SearchResultData
import ru.itis.application7.core.feature.listcontent.state.ListContentScreenEvent
import ru.itis.application7.core.feature.listcontent.state.ListContentScreenState
import ru.itis.application7.core.utils.properties.ExceptionsMessages
import ru.itis.application7.core.utils.properties.OtherProperties
import ru.itis.application7.core.utils.properties.RemoteConfigKey
import javax.inject.Inject

@HiltViewModel
class ListContentViewModel @Inject constructor(
    private val getListCurrentWeatherByListCitiesNamesUseCase: GetListCurrentWeatherByListCitiesNamesUseCase,
    private val sharedPref: SharedPreferences,
) : ViewModel() {

    private val _pageState = MutableStateFlow<ListContentScreenState>(value = ListContentScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    var numberOfLoadingItems = 0
    private val weatherCache = WeatherCache(cooldown = 60) //в секундах

    fun reduce(event: ListContentScreenEvent) {
        when (event) {
            is ListContentScreenEvent.OnSearchQueryChanged -> getCurrentWeatherByCitiesNames(event.query)
            is ListContentScreenEvent.OnLogOut -> logOutUser()
            is ListContentScreenEvent.OnAlertDialogClosed -> _pageState.value = ListContentScreenState.Initial
            is ListContentScreenEvent.OnItemClicked -> isDetailScreenEnabled(event.onItemSuccessClick)
            else -> throw UnknownEventException(ExceptionsMessages.UNKNOWN_EVENT)
        }
    }

    private fun isDetailScreenEnabled(onItemSuccessClicked: () -> Unit) {
        viewModelScope.launch {
            Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    _pageState.value = ListContentScreenState.Error(message = ExceptionsMessages.COMMON_ERROR)
                }
                val flag = Firebase.remoteConfig.getBoolean(RemoteConfigKey.IS_DETAIL_SCREEN_ENABLED)
                if (!flag) {
                    _pageState.value = ListContentScreenState.Error(message = ExceptionsMessages.FUNCTIONALITY_IS_NOT_AVAILABLE)
                } else {
                    onItemSuccessClicked()
                }
            }
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
                    //хотим посчитать по каким городам есть кэш.
                    //Если в конце число городов, по которым есть кэш, не совпадет с citiesNames.size,
                    //то нужно будет обновить кэш для всех городов
                    var cityCacheCount = 0
                    for (cityName in citiesNames) {
                        if (weatherCache.isCacheValid(cityName)) {
                            cityCacheCount++
                        }
                    }

                    if (cityCacheCount != citiesNames.size) { //идем к апи
                        //тут два случая: кэша нет, потому что его нет вообще, либо он истек.
                        // В первом нам нужно положить новый кеш, во втором - обновить существующий
                        val listWeatherModel = getListCurrentWeatherByListCitiesNamesUseCase(citiesNames)
                        listWeatherModel.forEach { weatherModel ->
                            if (weatherCache.isCacheExist(weatherModel.cityName)) {
                                weatherCache.refreshCache(weatherModel.cityName, weatherModel)
                            } else {
                                weatherCache.put(weatherModel.cityName, weatherModel)
                            }
                        }
                        weatherCache.incrementRequestCount(citiesNames) //увеличим счетчик промежуточных запросов у всеъ элементов  кэше, кроме элементов из списка citiesNames

                        //Возвращаем результат поиска, который содержит итоговый список и источник(кэш или сеть)
                        SearchResultData(
                            listWeatherModel = listWeatherModel,
                            source = OtherProperties.SOURCE_API,
                        )
                    } else { //идем в кэш
                        val result = mutableListOf<CurrentWeatherModel>()
                        citiesNames.forEach { cityName ->
                            weatherCache.get(cityName)?.let { cache ->
                                result.add(cache.payload)
                            }
                        } //тк это не поход к апи, мы не считаем это запросом и не увеличиваем никакие счетчики

                        //Возвращаем результат!!!
                        SearchResultData(
                            listWeatherModel = result,
                            source = OtherProperties.SOURCE_CACHE,
                        )
                    }
                }.onSuccess { searchResultData ->
                    _pageState.value = ListContentScreenState.SearchResult(result = searchResultData)
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