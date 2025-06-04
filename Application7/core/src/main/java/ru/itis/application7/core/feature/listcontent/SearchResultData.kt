package ru.itis.application7.core.feature.listcontent

import androidx.compose.runtime.Immutable
import ru.itis.application7.core.domain.model.CurrentWeatherModel

@Immutable
data class SearchResultData(
    val listWeatherModel: List<CurrentWeatherModel>,
    val source: String,
)