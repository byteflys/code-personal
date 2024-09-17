package com.android.code.commons

import android.app.Application

class CommonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Global.application = this
    }
}