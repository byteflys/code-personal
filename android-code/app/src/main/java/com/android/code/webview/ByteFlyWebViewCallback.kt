package com.android.code.webview

interface ByteFlyWebViewCallback {

    fun onWebViewTimeout() {}

    fun onDocumentStartLoading() {}

    fun onDocumentFinishLoading() {}
}