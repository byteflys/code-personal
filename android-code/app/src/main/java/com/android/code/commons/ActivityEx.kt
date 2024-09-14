package com.android.code.commons

import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.android.code.commons.BuildEx.isApiLevelAbove

object ActivityEx {

    fun AppCompatActivity.isFront(): Boolean {
        return lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    fun Activity.setFullScreenStyle() {
        if (isApiLevelAbove(Build.VERSION_CODES.R)) {
            window.decorView.windowInsetsController
            window.decorView.windowInsetsController?.hide(WindowInsets.Type.navigationBars())
            window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
            window.setDecorFitsSystemWindows(false)
            return
        }
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    fun AppCompatActivity.isNavBarVisible(): Boolean {
        val size = Point()
        val realSize = Point()
        val defaultDisplay = this.windowManager.defaultDisplay
        defaultDisplay.getSize(size)
        defaultDisplay.getRealSize(realSize)
        return realSize.y != size.y
    }
}