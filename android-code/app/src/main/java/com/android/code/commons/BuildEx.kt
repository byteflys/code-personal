package com.android.code.commons

import android.os.Build

object BuildEx {

    fun isApiLevelAbove(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT >= apiLevel
    }

    fun isApiLevelBelow(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT < apiLevel
    }
}