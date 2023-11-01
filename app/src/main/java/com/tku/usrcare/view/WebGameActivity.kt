package com.tku.usrcare.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.tku.usrcare.R
import com.tku.usrcare.databinding.ActivityWebGameBinding
import com.tku.usrcare.repository.SessionManager

class WebGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebGameBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebGameBinding.inflate(layoutInflater)
        setContentView(binding!!.root)



        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        // 初始化按鈕
        val buttonBack = binding.buttonBack

        this.onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            }
            else{
                finish()
            }
        }

        // 為按鈕添加點擊事件監聽器
        buttonBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            }
            else{
                this.finish()
            }
        }


        val myParam = SessionManager(this).getUserToken().toString()


        // WebView 資源載入
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build()
        webView.webViewClient = LocalContentWebViewClient(assetLoader)

        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html?param=$myParam")
    }
}

private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
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