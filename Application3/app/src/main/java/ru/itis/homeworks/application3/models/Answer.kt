package ru.itis.homeworks.application3.models

data class Answer(
    val id: Int,
    val text: String,
    var isSelected: Boolean = false
)
