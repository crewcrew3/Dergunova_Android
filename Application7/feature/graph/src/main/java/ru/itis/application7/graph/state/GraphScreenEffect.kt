package ru.itis.application7.graph.state

sealed interface GraphScreenEffect {
    data class Error(val message: String?) : GraphScreenEffect
}