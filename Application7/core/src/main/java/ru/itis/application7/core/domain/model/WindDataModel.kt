package ru.itis.application7.core.domain.model

data class WindDataModel(
    val speed: Float,
) {
    companion object {
        val EMPTY = WindDataModel(
            speed = -1f,
        )
    }
}
