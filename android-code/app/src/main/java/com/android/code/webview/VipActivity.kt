package com.android.code.webview

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.android.code.commons.ActivityEx.setFullScreenStyle
import com.android.code.commons.JSONEx.toJson
import com.android.code.commons.ToastEx
import com.android.code.module.idRes
import com.android.code.module.layoutRes

class VipActivity : ComponentActivity() {

    private lateinit var webView: ByteFlyWebView
    private lateinit var urlEdit: EditText
    private lateinit var tokenEdit: EditText
    private lateinit var goButton: Button

    private val defaultUrl = "https://galaxy.mystarnote.cn/user-center"
    private val defaultToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NTY4MTA5LCJ3eFVuaW9uaWQiOiJvSXViWXZpYl9sMkEzOXAyRFJHaDFiMFZtQks0IiwibG9naW5UeXBlIjoicGhvbmUiLCJpYXQiOjE3MjQ0NzA0NTUsImV4cCI6MTc0MDAyMjQ1NX0.swT0GFtt3RMvd1v8tYaFRS03OGpML6oqPK6SOqRkxoQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullScreenStyle()
        super.onCreate(savedInstanceState)
        setContentView(layoutRes.activity_vip)
        initView()
        addEvent()
    }

    private fun initView() {
        webView = findViewById(idRes.webView)
        urlEdit = findViewById(idRes.url)
        tokenEdit = findViewById(idRes.token)
        goButton = findViewById(idRes.button)
        urlEdit.setText(defaultUrl)
        tokenEdit.setText(defaultToken)
    }

    private fun addEvent() {
        goButton.setOnClickListener {
            loadWebpage()
        }
    }

    private fun loadWebpage() {
        val callback = object : ByteFlyWebViewCallback {

            @JavascriptInterface
            fun getHeaders() = createHeaders().toJson()

            @JavascriptInterface
            fun getHistoryNoteCount() = 999

            @JavascriptInterface
            fun gotoAccountSettings() {
                ToastEx.show("gotoAccountSettings")
            }

            @JavascriptInterface
            override fun onDocumentFinishLoading() {
                ToastEx.show("onDocumentFinishLoading")
            }

            @JavascriptInterface
            fun onTokenExpired() {
                ToastEx.show("onTokenExpired")
            }

            @JavascriptInterface
            fun onJoinVip() {
                ToastEx.show("onJoinVip")
            }

            override fun onWebViewTimeout() {
                ToastEx.show("onWebViewTimeout")
            }
        }
        val url = urlEdit.text.toString()
        webView.setWebViewCallback(callback)
        webView.registerNativeObject("native", callback)
        webView.openWebpage(url)
    }

    private fun createHeaders() = mutableMapOf<String, String>().apply {
        this["authorization"] = tokenEdit.text.toString()
        this["deviceId"] = "b1807ccb15454b5e"
        this["clientId"] = "b1807ccb15454b5e"
        this["client-type"] = "galaxy"
        this["language"] = "zh"
        this["device-lang"] = "zh_CN"
        this["version"] = "5057"
        this["channel"] = "ONYX"
    }
}