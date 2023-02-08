package com.example.kotlinjetpackdogs.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kotlinjetpackdogs.R
import com.example.kotlinjetpackdogs.view.MainActivity

class NotificationsHelper(val context: Context) {

    private val CHANNEL_ID = "Dogs channel id"
    private val NOTOFICATION_ID = 123


    fun createNotification() {
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val icon = R.drawable.dog_icon
        val bigIcon = BitmapFactory.decodeResource(context.resources, R.drawable.dog_icon)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setLargeIcon(bigIcon)
            .setContentTitle("Dogs retrieved")
            .setContentText("COntent")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bigIcon)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(NOTOFICATION_ID, notification)
        Log.d("notif", "notified")
    }


    private fun createNotificationChannel() {
        val name = CHANNEL_ID
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}