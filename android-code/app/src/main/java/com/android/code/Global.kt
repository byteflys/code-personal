package com.android.code

import android.app.Application
import android.os.Handler
import android.os.Looper

object Global {

    const val TAG = "AndroidCode"

    lateinit var application: Application

    val handler = Handler(Looper.getMainLooper())
}