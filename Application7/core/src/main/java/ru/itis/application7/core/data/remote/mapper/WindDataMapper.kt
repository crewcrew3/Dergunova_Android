package ru.itis.application7.core.data.remote.mapper

import ru.itis.application7.core.domain.model.WindDataModel
import ru.itis.application7.core.network.pojo.response.WindData
import javax.inject.Inject

class WindDataMapper @Inject constructor() {
    fun map(input: WindData?): WindDataModel {
        return input?.let { windData ->
            WindDataModel(
                speed = windData.speed ?: -1f,
            )
        } ?: WindDataModel.EMPTY
    }
}