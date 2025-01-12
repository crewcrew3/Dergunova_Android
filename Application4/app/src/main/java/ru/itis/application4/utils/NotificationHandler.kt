package ru.itis.application4.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.itis.application4.MainActivity
import ru.itis.application4.Properties
import ru.itis.application4.notifications.ChannelConstants
import ru.itis.application4.notifications.models.NotificationData
import ru.itis.application4.notifications.NotificationType
import ru.itis.application4.R

class NotificationHandler(
    private val appContext: Context
) {

    private val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelsIfNeeded()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O) //этот метод будет вызываться только тогда, когда апи будет не меньше 26
    fun createNotificationChannelsIfNeeded() {
        ChannelConstants.notificationsChannelsData.forEach { channelData ->
            if (notificationManager.getNotificationChannel(channelData.id) == null) {
                val channel = with (channelData) {
                    NotificationChannel(id, name, importance)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    fun showNotification(notificationData: NotificationData) {
        val index = when (notificationData.notificationType) {
            NotificationType.HIGH -> 1
            NotificationType.DEFAULT -> 2
            NotificationType.LOW -> 3
            else -> 0 //Max
        }
        val channelID = ChannelConstants.notificationsChannelsData[index].id

        /*
        ::class - оператор, который используется для получения ссылки на класс. Он возвращает объект типа KClass, который представляет класс MainActivity.
         .java -  свойство, которое позволяет получить объект Class в формате Java из объекта KClass. Это необходимо, потому что Intent принимает в качестве второго параметра объект типа Class, а не KClass.
        */

        val activityIntent = Intent(appContext, MainActivity::class.java).apply {
            putExtra(Properties.EXTRAS_KEY_LAUNCHED_FROM_NOTIFICATION, appContext.getString(R.string.launched_from_notification))
        }

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            Properties.REQUEST_CODE_INTENT_0,
            activityIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = with (notificationData) {
            NotificationCompat.Builder(appContext, channelID)
                .setSmallIcon(R.drawable.ic_mouse)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        }

        notificationManager.notify(notificationData.id, notification)
    }

    fun cancelNotification(notificationID: Int) {
        notificationManager.cancel(notificationID)
    }
}