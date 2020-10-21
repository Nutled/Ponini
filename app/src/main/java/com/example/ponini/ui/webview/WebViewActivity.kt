package com.example.ponini.ui.webview

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.ponini.databinding.ActivityWebviewBinding


/**
 * Created by Viнt@rь on 28.01.2020
 */
class WebViewActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_SELECT_FILE = 1
    }

    private lateinit var binding: ActivityWebviewBinding
    private lateinit var uploadMessage: ValueCallback<Array<Uri>>
    private lateinit var viewModel: WebViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) < BuildConfig.WEB_VIEW_DATE) {
            finish()

            val intent = Intent(this@WebViewActivity, MainActivity::class.java)
            startActivity(intent)
            return
        }*/
        viewModel = ViewModelProvider(this).get(WebViewViewModel::class.java)

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