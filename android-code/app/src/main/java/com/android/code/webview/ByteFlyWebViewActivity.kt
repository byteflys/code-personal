package com.android.code.webview

import android.os.Bundle
import android.webkit.WebView.setWebContentsDebuggingEnabled
import androidx.activity.ComponentActivity
import com.android.code.commons.ActivityEx.setFullScreenStyle
import com.android.code.commons.ToastEx

class ByteFlyWebViewActivity : ComponentActivity() {

    lateinit var webView: ByteFlyWebView

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullScreenStyle()
        super.onCreate(savedInstanceState)
        webView = ByteFlyWebView(this)
        setContentView(webView)
        setWebContentsDebuggingEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        webView.openWebpage("https://www.baidu.com")
        webView.executeJavascriptSentence("window.onBackPressed", Unit, Map::class.java) {
            ToastEx.show(it.toString())
        }
    }
}