package ru.itis.application7.core.domain.exception

class CombinedWeatherException(
    val errors: List<Throwable>
) : Throwable (
    errors.joinToString("\n") { "${it.message}" }
)