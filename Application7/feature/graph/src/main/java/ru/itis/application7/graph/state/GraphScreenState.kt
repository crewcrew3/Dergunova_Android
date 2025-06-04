package ru.itis.application7.graph.state

import androidx.compose.runtime.Immutable

sealed interface GraphScreenState {
    data object Initial : GraphScreenState
    data object ErrorNumberPointInput: GraphScreenState
    data object ErrorPointsValuesInput: GraphScreenState
    @Immutable
    data class Success(val result: List<Float>): GraphScreenState
}