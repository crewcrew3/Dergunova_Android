package ru.itis.application7.core.feature.listcontent

import ru.itis.application7.core.domain.model.CurrentWeatherModel

data class SearchResultData(
    val listWeatherModel: List<CurrentWeatherModel>,
    val source: String,
)