package ru.itis.homeworks.application3.models

data class Question(
    val id: Int,
    val text: String,
    val answers: List<Answer>
)
