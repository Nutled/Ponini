package com.game.ponini.ui.webview

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.game.ponini.databinding.ActivityWebviewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Viнt@rь on 28.01.2020
 */
@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_SELECT_FILE = 1
    }

    private val viewModel: WebViewViewModel by viewModels()

    private lateinit var binding: ActivityWebviewBinding
    private lateinit var uploadMessage: ValueCallback<Array<Uri>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) < BuildConfig.WEB_VIEW_DATE) {
            finish()

            val intent = Intent(this@WebViewActivity, MainActivity::class.java)
            startActivity(intent)
            return
        }*/
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.test.observe(this, {
            Log.d("TEST", "test response")
        })

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
        }

/*        viewModel.deepLink.observe(this, {
            binding.webview.apply {
                settings.userAgentString += " |${BuildConfig.APPLICATION_ID}|${Locale.getDefault().country}|${it}"
                settings.userAgentString = settings.userAgentString.replace("; wv", "")
                //loadUrl(BuildConfig.URL)

                CookieManager.getInstance().setAcceptCookie(true)
            }
        })*/
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