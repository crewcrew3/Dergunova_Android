package ru.itis.application7.core.data.remote.mapper

import ru.itis.application7.core.domain.model.WeatherDataModel
import ru.itis.application7.core.network.pojo.response.WeatherData
import ru.itis.application7.core.utils.OtherProperties
import javax.inject.Inject

class WeatherDataMapper @Inject constructor() {
    fun map(input: List<WeatherData?>?): List<WeatherDataModel> {
        return input?.let { list ->
            val result = mutableListOf<WeatherDataModel>()
            list.forEach { item ->
                item?.let {
                    result.add(
                        WeatherDataModel(
                            main = item.main ?: OtherProperties.EMPTY_WEATHER,
                            description = item.description ?: OtherProperties.EMPTY_WEATHER_DESC,
                        )
                    )
                } ?: WeatherDataModel.EMPTY
            }
            result
        } ?: emptyList()
    }
}