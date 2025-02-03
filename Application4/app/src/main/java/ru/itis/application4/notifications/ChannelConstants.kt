package ru.itis.application4.notifications

import android.app.NotificationManager
import ru.itis.application4.notifications.models.NotificationChannelData

object ChannelConstants {

    val notificationsChannelsData = listOf(
        NotificationChannelData(
            id = "Max",
            name = "Самые важные уведомления",
            importance = NotificationManager.IMPORTANCE_MAX,
        ),
        NotificationChannelData(
            id = "High",
            name = "Важные уведомления",
            importance = NotificationManager.IMPORTANCE_HIGH,
        ),
        NotificationChannelData(
            id = "Default",
            name = "Уведомления с важностью по умолчанию",
            importance = NotificationManager.IMPORTANCE_DEFAULT,
        ),
        NotificationChannelData(
            id = "Low",
            name = "Не очень важные уведомления",
            importance = NotificationManager.IMPORTANCE_LOW,
        ),
    )

}