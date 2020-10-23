package com.game.ponini.ui.webview

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.game.ponini.data.TestRepository
import com.game.ponini.model.main.MainRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response

/**
 * Created by Viнt@rь on 28.01.2020
 */
class WebViewViewModel @ViewModelInject constructor(
        @ApplicationContext
        private val context: Context,
        private val repository: TestRepository
) : ViewModel() {

    val test: LiveData<Response<Void>> = liveData {
        emit(repository.game(MainRequest("", "", "", emptyList())))
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