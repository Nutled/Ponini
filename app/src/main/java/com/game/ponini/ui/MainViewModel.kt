package com.game.ponini.ui

import android.content.Context
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.game.ponini.BuildConfig
import com.game.ponini.data.MainRepository
import com.game.ponini.domain.Event
import com.game.ponini.model.main.MainRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response

/**
 * Created by Viнt@rь on 21.10.2020
 */
class MainViewModel @ViewModelInject constructor(
        @ApplicationContext
        private val context: Context,
        private val repository: MainRepository
) : ViewModel() {

    private val _reconnectAction = MutableLiveData<Event<Unit>>()

    val result: LiveData<Response<Void>> = _reconnectAction.switchMap {
        liveData {
            val deepLinkFB = _deepLinkFB.value
            val deepLinkAPS = _deepLinkAPS.value
            val accelerometer = _accelerometer.value
            emit(repository.game(MainRequest(deepLinkFB, deepLinkAPS, accelerometer)))
        }
    }

    private val _accelerometer = MutableLiveData<List<Double>>()
    //val accelerometer: LiveData<List<Double>> get() = _accelerometer

    private val _deepLinkFB = MutableLiveData<String>() // facebook
    //val deepLinkFB: LiveData<String> get() = _deepLinkFB

    private val _deepLinkAPS = MutableLiveData<String>() // appsflyer
    //val deepLinkAPS: LiveData<String> get() = _deepLinkAPS

    init {
        _reconnectAction.value = Event(Unit)
        /*AppLinkData.fetchDeferredAppLinkData(context) { // TODO uncomment this when facebook api key will be added to project
            if (it == null) {
                InstallReferrerClient.newBuilder(context).build().apply {
                    startConnection(object : InstallReferrerStateListener {
                        override fun onInstallReferrerSetupFinished(responseCode: Int) {
                            when (responseCode) {
                                InstallReferrerClient.InstallReferrerResponse.OK -> {
                                    _deepLinkFB.value = installReferrer.installReferrer
                                    endConnection()
                                }
                            }
                        }

                        override fun onInstallReferrerServiceDisconnected() {
                        }
                    })
                }
            } else {
                _deepLinkFB.postValue(it.ref)
            }
        }*/

        val listener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    cvData.map {
                        Log.i("MainViewModel", "conversion_attribute: ${it.key} = ${it.value}")
                    }
                }
            }

            override fun onConversionDataFail(p0: String?) {
            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                data?.map {
                    Log.d("MainViewModel", "onAppOpen_attribute: ${it.key} = ${it.value}")
                }
            }

            override fun onAttributionFailure(p0: String?) {
            }
        }
        AppsFlyerLib.getInstance().init(BuildConfig.APPSFLYER_API_KEY, listener, context)
    }

    fun reconnect() {
        _reconnectAction.value = Event(Unit)
    }
}