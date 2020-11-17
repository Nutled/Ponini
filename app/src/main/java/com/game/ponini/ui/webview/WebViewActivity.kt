package com.game.ponini.ui.webview

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.game.ponini.databinding.ActivityWebviewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Viнt@rь on 28.01.2020
 */
@RequiresApi(23)
@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_SELECT_FILE = 1

        const val KEY_LOCATION = "location"
    }

    private lateinit var binding: ActivityWebviewBinding
    private lateinit var uploadMessage: ValueCallback<Array<Uri>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webview.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            webViewClient = object : WebViewClient() {

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler,
                    error: SslError
                ) {
                    if (url == error.url) {
                        handler.proceed()
                    } else {
                        handler.cancel()
                    }
                }
            }
            webChromeClient = object : WebChromeClient() {

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    uploadMessage = filePathCallback!!

                    val intent = fileChooserParams?.createIntent()
                    startActivityForResult(intent, REQUEST_SELECT_FILE)
                    return true
                }
            }
            CookieManager.getInstance().setAcceptCookie(true)

            loadUrl(intent.getStringExtra(KEY_LOCATION)!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_FILE) {
            uploadMessage.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    data
                )
            )
        }
    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        }
    }
}