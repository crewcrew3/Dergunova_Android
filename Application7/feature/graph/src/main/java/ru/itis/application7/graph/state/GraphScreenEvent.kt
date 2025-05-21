package ru.itis.application7.graph.state

sealed interface GraphScreenEvent {
    data class DrawGraph(val numberOfPoints: String, val pointsValues: String): GraphScreenEvent
}