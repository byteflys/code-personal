package com.android.code.webview

import android.util.Log
import android.webkit.JavascriptInterface
import com.android.code.commons.ToastEx

// delegate javascript console to android logcat
object ByteFlyWebViewDebugger {

    @JavascriptInterface
    fun log(obj: String?) {
        Log.d(ByteFlyWebViewDebugger::class.simpleName, obj.toString())
    }

    @JavascriptInterface
    fun error(obj: String?) {
        Log.e(ByteFlyWebViewDebugger::class.simpleName, obj.toString())
    }

    @JavascriptInterface
    fun alert(obj: String?) {
        ToastEx.show(obj.orEmpty())
    }
}