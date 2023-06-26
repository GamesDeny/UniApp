package com.example.timer.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.timer.MainActivity
import com.example.timer.R

class NotificationHandler(private val context: Context) {
    private lateinit var notificationManager: NotificationManager
    private val tag = "NotificationHandler"

    fun createNotification(aMessage: String?) {
        val id = context.getString(R.string.channel_id)
        val title = context.getString(R.string.channel_name)

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mChannel = getMChannel(id, title)

        notificationManager.createNotificationChannel(mChannel)
        notificationManager.notify(
            0, NotificationCompat.Builder(context, id)
                .setContentTitle(aMessage)
                .setSmallIcon(androidx.core.R.drawable.notification_bg)
                .setContentText(context.getString(R.string.default_notification_message))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent())
                .setTicker(aMessage)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .build()
        )
        Log.d(tag, "Notification sent")
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getMChannel(id: String, title: String): NotificationChannel {
        val channel = NotificationChannel(id, title, NotificationManager.IMPORTANCE_HIGH)

        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        return channel
    }

}