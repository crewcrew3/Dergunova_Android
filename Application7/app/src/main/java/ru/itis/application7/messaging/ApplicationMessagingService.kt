package ru.itis.application7.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.application7.MainActivity
import ru.itis.application7.R
import ru.itis.application7.core.utils.AppLifecycleTracker
import ru.itis.application7.core.utils.properties.OtherProperties

@AndroidEntryPoint
class ApplicationMessagingService(
    private val sharedPref: SharedPreferences,
) : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        when(message.data[MESSAGE_DATA_HEADER_CATEGORY]) {
            CATEGORY_HIGH_PRIORITY -> handleHighPriorityPush(message)
            CATEGORY_PREFERENCES -> handlePreferencesPush(message)
            CATEGORY_GRAPH -> handleGraphPush(message)
        }
    }

    private fun handleHighPriorityPush(message: RemoteMessage) {
        val title = message.data[MESSAGE_DATA_HEADER_TITLE] ?: return
        val messageText = message.data[MESSAGE_DATA_HEADER_MESSAGE] ?: return

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(messageText)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun handlePreferencesPush(message: RemoteMessage) {
        val category = message.data[MESSAGE_DATA_HEADER_CATEGORY] ?: return
        val data = message.data[MESSAGE_DATA_HEADER_DATA] ?: return

        sharedPref
            .edit()
            .putString(OtherProperties.SHARED_PREF_CATEGORY, category)
            .putString(OtherProperties.SHARED_PREF_DATA, data)
            .apply()
    }

    private fun handleGraphPush(message: RemoteMessage) {

        if (!AppLifecycleTracker.isAppInForeground()) return

        val isAuth = sharedPref.getString(OtherProperties.USER_NICK_SHARED_PREF, null) != null
        if (!isAuth) {
            showToast(TOAST_MESSAGE_UNAUTHORIZED)
            return
        }

        //FLAG_ACTIVITY_NEW_TASK - если вызываем startActivity() из контекста, который не является Activity (из сервиса).
        //FLAG_ACTIVITY_SINGLE_TOP - Если Activity уже запущена в верхней части стека, то система не создаст новый экземпляр Activity.
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(OtherProperties.INTENT_START_DESTINATION_KEY, OtherProperties.GRAPH_ROUTE_STR_KEY)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("Test TAG - $token")
    }

    companion object {
        private const val CHANNEL_ID = "main_channel"
        private const val CHANNEL_NAME = "Main Channel"
        private const val NOTIFICATION_ID = 1

        const val CATEGORY_HIGH_PRIORITY = "high_priority"
        const val CATEGORY_PREFERENCES = "preferences"
        const val CATEGORY_GRAPH = "graph"

        const val MESSAGE_DATA_HEADER_CATEGORY = "category"
        const val MESSAGE_DATA_HEADER_TITLE = "title"
        const val MESSAGE_DATA_HEADER_MESSAGE = "message"
        const val MESSAGE_DATA_HEADER_DATA = "data"

        const val TOAST_MESSAGE_UNAUTHORIZED = "Необходима авторизация"
        const val TOAST_MESSAGE_YOU_ALREADY_ON_SCREEN = "Вы уже находитесь на экране графика"
    }
}