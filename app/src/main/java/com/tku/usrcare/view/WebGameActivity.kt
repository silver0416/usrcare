package com.tku.usrcare.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.google.gson.Gson
import com.tku.usrcare.R
import com.tku.usrcare.databinding.ActivityWebGameBinding
import com.tku.usrcare.model.SudokuPuzzleData
import com.tku.usrcare.repository.SessionManager
import com.tku.usrcare.view.component.TitleBox

class WebGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebGameBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding = ActivityWebGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        binding.topBar.setContent {
            TitleBox(
                color = colorResource(id = R.color.btnBrainGameColor),
                title = stringResource(id = R.string.brain_game),
                icon = painterResource(id = R.drawable.ic_game),
                webView = webView
            )
        }

        this.onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }


        val myParam = SessionManager(this).getUserToken().toString()


        // WebView 資源載入
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build()
        webView.webViewClient = LocalContentWebViewClient(assetLoader)
        webView.addJavascriptInterface(WebAppInterface(this), "AndroidInterface")

        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html?param=$myParam")
    }
}

private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) :
    WebViewClientCompat() {
    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        return assetLoader.shouldInterceptRequest(request.url)
    }

    @Deprecated("Deprecated in Java")
    override fun shouldInterceptRequest(
        view: WebView,
        url: String
    ): WebResourceResponse? {
        return assetLoader.shouldInterceptRequest(Uri.parse(url))
    }
}

class WebAppInterface(private val context: Context) {
    @JavascriptInterface
    fun processWebData(data: String) {
        // 在這裡處理從 WebView 傳來的資料
        val sudokuPuzzleData = Gson().fromJson(data, SudokuPuzzleData::class.java)
    }
}