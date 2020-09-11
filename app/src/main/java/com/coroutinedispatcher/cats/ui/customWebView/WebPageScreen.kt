package com.coroutinedispatcher.cats.ui.customWebView

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPageScreen(urlToRender: String) {
    AndroidView(viewBlock = ::WebView) { webView ->
        with(webView){
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(urlToRender)
        }
    }
}