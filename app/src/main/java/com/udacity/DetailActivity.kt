package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.utility.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()

        fab.setOnClickListener {
            finish()
        }

        val filename: String? = intent.getStringExtra(FILE_NAME)
        val status: String? = intent.getStringExtra(STATUS)

        filename_text.text = filename
        status_text.text = status

        setSupportActionBar(toolbar)
    }


    companion object {
        const val FILE_NAME = "FILE_NAME"
        const val STATUS = "STATUS"
    }

}

