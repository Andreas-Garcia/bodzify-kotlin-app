package com.bpm.a447bpm.controller

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bpm.a447bpm.R
import com.bpm.a447bpm.adapter.SongListAdapter
import com.bpm.a447bpm.api.ApiClient
import com.bpm.a447bpm.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.webview)

        webView = findViewById(R.id.webView)

        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/html/post-test.html")
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
    }
}