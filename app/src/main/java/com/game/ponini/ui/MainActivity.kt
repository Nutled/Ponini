package com.game.ponini.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.game.ponini.BuildConfig
import com.game.ponini.R
import com.game.ponini.databinding.ActivityMainBinding
import com.game.ponini.ui.webview.WebViewActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }
        setContentView(binding.root)

        // TODO replace it to service
        viewModel.pushResult.observe(this, {
            val channelId = "default_channel_id"

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setAutoCancel(true)
                    .setLargeIcon(it.image)
                    .setLights(Color.WHITE, 1000, 2000)
                    .setContentTitle(it.title)
                    .setContentText(it.text)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            setSmallIcon(IconCompat.createWithBitmap(it.icon))
                        } else {
                            setSmallIcon(R.mipmap.ic_launcher)
                        }
                    }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                        NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(0, notificationBuilder.build())
        })

        viewModel.result.observe(this, {
            when {
                it.code() >= 500 -> {
                    MaterialAlertDialogBuilder(this)
                            .setCancelable(false)
                            .setTitle("Error")
                            .setMessage("Connection error")
                            .setPositiveButton("Reconnect") { _, _ ->
                                viewModel.reconnect()
                            }
                            .show()
                }
                it.code() == 302 -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (it.errorBody()?.string() == BuildConfig.APPLICATION_ID) {
                            finish()

                            val intent = Intent(this, WebViewActivity::class.java)
                            intent.putExtra(WebViewActivity.KEY_LOCATION, it.headers()[WebViewActivity.KEY_LOCATION])
                            startActivity(intent)
                        }
                    }
                }
                else -> { // open game

                }
            }
        })
    }
}