package com.udacity.utility

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

// Notification ID.
private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    description: String,
    status: String
) {


    val contentIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        putExtra(DetailActivity.FILE_NAME, description)
        putExtra(DetailActivity.STATUS, status)
    }

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )


    val downloadImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.download_icon
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.channel_id)
    )

        .setSmallIcon(R.drawable.download_icon)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setStyle(bigPicStyle)
        .setLargeIcon(downloadImage)

        .addAction(
            R.drawable.download_icon,
            applicationContext.getString(R.string.go_to_details),
            contentPendingIntent
        )

        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())

}


fun NotificationManager.cancelNotifications() {
    cancelAll()
}