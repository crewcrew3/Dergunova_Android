package ru.itis.application7.core.feature.detailinfo.state

sealed interface DetailInfoScreenEvent {
    data class OnScreenInit(val query: String) : DetailInfoScreenEvent
    data object OnAlertDialogClosed : DetailInfoScreenEvent
}