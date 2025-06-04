package ru.itis.application7.graph.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.application7.core.utils.properties.ExceptionsMessages
import ru.itis.application7.graph.state.GraphScreenEffect
import ru.itis.application7.graph.state.GraphScreenEvent
import ru.itis.application7.graph.state.GraphScreenState
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor() : ViewModel() {

    private val _pageState = MutableStateFlow<GraphScreenState>(value = GraphScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<GraphScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    private val _graphData = MutableStateFlow<List<Float>?>(null)
    val graphData = _graphData.asStateFlow()

    fun reduce(event: GraphScreenEvent) {
        when (event) {
            is GraphScreenEvent.DrawGraph -> draw(event.numberOfPoints, event.pointsValues)
        }
    }

    private fun draw(numberOfPoints: String, pointsValues: String) {
        viewModelScope.launch {
            val regexDigit = Regex(REGEX_FOR_DIGIT)
            if (regexDigit.matches(numberOfPoints)) {
                val values = pointsValues
                    .split(",")
                    .mapNotNull { it.trim().toFloatOrNull() }
                    .filter { it >= 0 }
                if (values.size == numberOfPoints.toInt()) {
                    _pageState.value = GraphScreenState.Success(result = values)
                } else {
                    _pageEffect.emit(GraphScreenEffect.Error(ExceptionsMessages.GRAPH_ERROR))
                    _pageState.value = GraphScreenState.ErrorPointsValuesInput
                }
            } else {
                _pageState.value = GraphScreenState.ErrorNumberPointInput
            }
        }
    }

    private companion object {
        const val REGEX_FOR_DIGIT = "^[1-9]\\d*$"
    }
}