package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.utility.cancelNotifications
import com.udacity.utility.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var description: String

    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)

        customButton.setOnClickListener {
            customButton.buttonState = ButtonState.Clicked
            when (radioGroup.checkedRadioButtonId) {
                -1 -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.describe_download),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.currentprojectradioButton -> {
                    customButton.buttonState = ButtonState.Loading
                    download(getString(R.string.C3_project_link))
                    description = getString(R.string.C3_project_link_description)
                }
                R.id.retrofit_radioButton -> {
                    customButton.buttonState = ButtonState.Loading
                    download(getString(R.string.retrofit_link))
                    description = getString(R.string.retrofit_link_description)
                }
                R.id.glide_repositoryradioButton -> {
                    customButton.buttonState = ButtonState.Loading
                    download(getString(R.string.glide_link))
                    description = getString(R.string.glide_link_description)
                }
            }
        }

        createNotificationChannel(
            getString(R.string.channel_id),
            getString(R.string.channel_name)
        )
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id) {
                val complete = getString(R.string.complete)
                customButton.buttonState = ButtonState.Completed

                val notificationManager = ContextCompat.getSystemService(
                    context, NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    getString(R.string.completed_download),
                    context,
                    description,
                    complete
                )
            }
        }
    }

    private fun download(url: String) {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelNotifications()

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(getString(R.string.app_title))
            .setDescription(getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        downloadID = downloadManager.enqueue(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(true)
        }

        notificationChannel.enableVibration(true)
        notificationChannel.description =
            applicationContext.getString(R.string.notification_channel_description)

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )

        notificationManager.createNotificationChannel(notificationChannel)
    }
}
