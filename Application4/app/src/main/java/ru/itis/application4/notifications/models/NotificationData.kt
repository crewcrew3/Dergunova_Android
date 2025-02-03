package ru.itis.application4.notifications.models

import ru.itis.application4.notifications.NotificationType

data class NotificationData(
    val id: Int,
    val title: String,
    val message: String,
    val notificationType: NotificationType? = null,
)
