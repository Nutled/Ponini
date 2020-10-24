package com.game.ponini.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.game.ponini.BuildConfig
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.pushResult.observe(this, {
        })

        viewModel.result.observe(this, { it ->
            when {
                it.code() >= 500 -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Error")
                        .setMessage("Connection error")
                        .setPositiveButton("Reconnect") { _, _ ->
                            viewModel.reconnect()
                        }
                        .show()
                    return@observe
                }
                it.code() == 302 -> {
                    val isHasPackageName = it.headers().any { it.second == BuildConfig.APPLICATION_ID }
                    if (isHasPackageName) {
                        finish()

                        val intent = Intent(this, WebViewActivity::class.java)
                        startActivity(intent)
                        return@observe
                    }
                }
                else -> { // open game

                }
            }
        })
    }
}