package com.game.ponini

import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Viнt@rь on 21.10.2020
 */
@HiltAndroidApp
class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        val config = YandexMetricaConfig.newConfigBuilder(BuildConfig.APPMETRICA_API_KEY).build()
        YandexMetrica.activate(this, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }
}
