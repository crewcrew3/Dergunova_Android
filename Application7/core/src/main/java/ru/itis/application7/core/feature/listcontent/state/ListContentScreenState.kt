package ru.itis.application7.core.feature.listcontent.state

import ru.itis.application7.core.feature.listcontent.SearchResultData

sealed interface ListContentScreenState {
    data object Initial : ListContentScreenState
    data object Loading : ListContentScreenState
    data object ErrorInput: ListContentScreenState
    data class SearchResult(val result: SearchResultData) : ListContentScreenState
    data class Error(val message: String?) : ListContentScreenState
    data class ErrorHttp(val message: List<String?>) : ListContentScreenState
}