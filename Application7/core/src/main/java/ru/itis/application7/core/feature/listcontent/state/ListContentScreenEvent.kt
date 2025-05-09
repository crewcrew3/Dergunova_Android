package ru.itis.application7.core.feature.listcontent.state

sealed interface ListContentScreenEvent {
    data class OnSearchQueryChanged(val query: String) : ListContentScreenEvent
    data object OnLogOut : ListContentScreenEvent
    data object OnAlertDialogClosed : ListContentScreenEvent
}