package com.game.ponini.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.game.ponini.BuildConfig
import com.game.ponini.data.MainRepository
import com.game.ponini.domain.Event
import com.game.ponini.model.main.MainRequest
import com.game.ponini.model.push.PushResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Created by Viнt@rь on 21.10.2020
 */
class MainViewModel @ViewModelInject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: MainRepository
) : ViewModel() {

    private val accelerometerListener = object : SensorEventListener {
        private val data = ArrayList<Float>()

        override fun onSensorChanged(event: SensorEvent?) {
            event?.values?.map {
                data.add(it)
            }

            accelerometer.value = data
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    private val accelerometer = MutableLiveData<List<Float>>()
    private val deepLinkFB = MutableLiveData<String>() // facebook
    private val deepLinkAPS = MutableLiveData<String>() // appsflyer

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val reconnectAction = MediatorLiveData<Event<Unit>>().apply {
        addSource(deepLinkFB) {
            sensorManager.unregisterListener(accelerometerListener)
            reconnect()
        }
        addSource(deepLinkAPS) {
            sensorManager.unregisterListener(accelerometerListener)
            reconnect()
        }
    }

    val result: LiveData<Response<Void>> = reconnectAction.switchMap {
        liveData {
            val deepLinkFB = deepLinkFB.value
            val deepLinkAPS = deepLinkAPS.value
            val accelerometer = accelerometer.value
            emit(repository.game(MainRequest(deepLinkFB, deepLinkAPS, accelerometer)))
        }
    }

    val pushResult: LiveData<PushResponse> = liveData {
        emit(repository.getPush())
    }

    init {
        AppLinkData.fetchDeferredAppLinkData(context) {
            if (it == null) {
                InstallReferrerClient.newBuilder(context).build().apply {
                    startConnection(object : InstallReferrerStateListener {
                        override fun onInstallReferrerSetupFinished(responseCode: Int) {
                            when (responseCode) {
                                InstallReferrerClient.InstallReferrerResponse.OK -> {
                                    deepLinkFB.value = installReferrer.installReferrer
                                    endConnection()
                                }
                            }
                        }

                        override fun onInstallReferrerServiceDisconnected() {
                        }
                    })
                }
            } else {
                deepLinkFB.postValue(it.ref)
            }
        }

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            accelerometerListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        val listener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    deepLinkAPS.value = cvData["http_referrer"] as String?
                }
            }

            override fun onConversionDataFail(error: String?) {
            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                data?.let { cvData ->
                    deepLinkAPS.value = cvData["http_referrer"]
                }
            }

            override fun onAttributionFailure(error: String?) {
            }
        }
        AppsFlyerLib.getInstance().init(BuildConfig.APPSFLYER_API_KEY, listener, context)
        AppsFlyerLib.getInstance().startTracking(context)
    }

    fun reconnect() {
        reconnectAction.value = Event(Unit)
    }
}