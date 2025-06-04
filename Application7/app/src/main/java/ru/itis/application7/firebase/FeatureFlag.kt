package ru.itis.application7.firebase

data class FeatureFlag(
    val name: String,
    val value: Any,
    val comment: String? = null,
)