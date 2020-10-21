package com.game.ponini.ui.webview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.facebook.applinks.AppLinkData

/**
 * Created by Viнt@rь on 28.01.2020
 */
class WebViewViewModel(application: Application) : AndroidViewModel(application) {

    private val _deepLinkFB = MutableLiveData<String>() // facebook
    val deepLinkFB: LiveData<String> get() = _deepLinkFB

    private val _deepLinkAF = MutableLiveData<String>() // appsflyer
    val deepLinkAF: LiveData<String> get() = _deepLinkAF

    init {
        AppLinkData.fetchDeferredAppLinkData(application) {
            if (it == null) {
                InstallReferrerClient.newBuilder(application).build().apply {
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
        }
    }
}