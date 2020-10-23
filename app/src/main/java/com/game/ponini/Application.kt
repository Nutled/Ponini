package com.game.ponini

import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Viнt@rь on 21.10.2020
 */
@HiltAndroidApp
class Application : android.app.Application(){

        override fun onCreate() {
            super.onCreate()
            val API_key_appmetrica: String = "53eb4b5d-3970-4b80-8d04-a812d2a49143"
            // Creating an extended library configuration.
            val config = YandexMetricaConfig.newConfigBuilder(API_key_appmetrica).build()
            // Initializing the AppMetrica SDK.
            YandexMetrica.activate(applicationContext, config)
            // Automatic tracking of user activity.
            YandexMetrica.enableActivityAutoTracking(this)
        }

}