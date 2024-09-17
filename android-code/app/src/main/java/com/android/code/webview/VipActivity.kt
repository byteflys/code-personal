package com.android.code.webview

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.code.commons.ActivityEx.setFullScreenStyle
import com.android.code.commons.Global
import com.android.code.commons.JSONEx.toJson
import com.android.code.module.idRes
import com.android.code.module.layoutRes

class VipActivity : ComponentActivity() {

    private lateinit var webView: ByteFlyWebView
    private lateinit var urlEdit: EditText
    private lateinit var tokenEdit: EditText
    private lateinit var goButton: Button
    private lateinit var logTextView: TextView

    private val defaultUrl = "https://dev-galaxy.send2boox.com/user-center"
    private val defaultToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Mzk4MjcsImxvZ2luVHlwZSI6InBob25lIiwiaWF0IjoxNzI0NDgyNTE2LCJleHAiOjE3NDAwMzQ1MTZ9.WS0fs1kLska29EDPFU0IpVZwf1JuWU6TDcb2oLIbIxE"

    private var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullScreenStyle()
        super.onCreate(savedInstanceState)
        setContentView(layoutRes.activity_vip)
        initView()
        addEvent()
        getWebkitVersion()
    }

    private fun initView() {
        webView = findViewById(idRes.webView)
        urlEdit = findViewById(idRes.url)
        tokenEdit = findViewById(idRes.token)
        goButton = findViewById(idRes.button)
        logTextView = findViewById(idRes.log)
        urlEdit.setText(defaultUrl)
        tokenEdit.setText(defaultToken)
    }

    private fun addEvent() {
        goButton.setOnClickListener {
            try {
                loadWebpage()
            } catch (e: Throwable) {
                val message = e.javaClass.typeName + "  " + e.message
                appendText(message)
            }
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
                appendText("gotoAccountSettings")
            }

            @JavascriptInterface
            override fun onDocumentFinishLoading() {
                appendText("onDocumentFinishLoading")
            }

            @JavascriptInterface
            fun onTokenExpired() {
                appendText("onTokenExpired")
            }

            @JavascriptInterface
            fun onJoinVip() {
                appendText("onJoinVip")
            }

            override fun onWebViewTimeout() {
                appendText("onWebViewTimeout")
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

    private fun getWebkitVersion() {
        val agent = webView.settings.userAgentString
        appendText(agent)
    }

    private fun appendText(append: String) {
        Global.handler.post {
            text = text + "\n" + append
            logTextView.text = text
        }
    }
}