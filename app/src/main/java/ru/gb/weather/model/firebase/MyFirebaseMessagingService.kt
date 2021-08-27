package ru.gb.weather.model.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.gb.weather.R

const val TAG = "@@@"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: ${remoteMessage.notification?.title}")

        val remoteMessageData = remoteMessage.data
        if (remoteMessageData.isNotEmpty()) {
            val data = remoteMessageData.toMap()
            val text1 = data[PUSH_KEY_1]
            val text2 = data[PUSH_KEY_2]
            Log.d(TAG, "Data received: $text1, $text2")
        }

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_message_24)
                setContentTitle(remoteMessage.notification?.title)
                setContentText(remoteMessage.notification?.body)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "FirebaseWeather"
        val descriptionText = "Firebase Weather application channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        Log.d("@@@", "onNewToken: $token")
    }

    companion object {
        private const val PUSH_KEY_1 = "key1"
        private const val PUSH_KEY_2 = "key2"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 37
    }
}
