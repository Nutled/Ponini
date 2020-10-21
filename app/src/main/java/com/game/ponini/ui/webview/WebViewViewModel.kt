package com.game.ponini.ui.webview

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.facebook.applinks.AppLinkData
import com.game.ponini.api.API
import com.game.ponini.model.test.TestRequest
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Created by Viнt@rь on 28.01.2020
 */
class WebViewViewModel @ViewModelInject constructor(
    @ApplicationContext
    private val context: Context,
    private val api: API
) : ViewModel() {

    val test: LiveData<Any> = liveData {
        emit(api.test(TestRequest("", "", "", emptyList())).execute().body()!!)
    }

    private val _deepLinkFB = MutableLiveData<String>() // facebook
    val deepLinkFB: LiveData<String> get() = _deepLinkFB

    private val _deepLinkAPS = MutableLiveData<String>() // appsflyer
    val deepLinkAPS: LiveData<String> get() = _deepLinkAPS

    init {
        /*AppLinkData.fetchDeferredAppLinkData(context) {
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
    }
}