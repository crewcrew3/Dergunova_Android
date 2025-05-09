package ru.itis.application7.core.data.remote.mapper

import ru.itis.application7.core.domain.model.MainDataModel
import ru.itis.application7.core.network.pojo.response.MainData
import javax.inject.Inject

class MainDataMapper @Inject constructor() {
    fun map(input: MainData?): MainDataModel {
        return input?.let { mainData ->
            MainDataModel(
                temp = mainData.temp ?: -100f,
                feelsLike = mainData.feelsLike ?: -100f,
                pressure = mainData.pressure ?: -1,
                humidity = mainData.humidity ?: -1,
            )
        } ?: MainDataModel.EMPTY
    }
}