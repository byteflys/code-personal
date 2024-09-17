package com.android.code.commons

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.android.code.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable

object ToastEx {

    fun show(
        message: String,
        withLongDuration: Boolean = false
    ) = showDefaultToast(message, withLongDuration)

    fun show(
        @StringRes stringResId: Int,
        withLongDuration: Boolean = false
    ) = showDefaultToast(Global.application.resources.getString(stringResId), withLongDuration)

    private fun showDefaultToast(
        message: String,
        withLongDuration: Boolean = false
    ) {
        val context = Global.application
        // load toast layout
        val root = LayoutInflater.from(context).inflate(R.layout.layout_toast_default, null)
        val wrapper = root.findViewById<View>(R.id.wrapper)
        val textView = root.findViewById<View>(R.id.text) as TextView
        textView.text = message
        // obtain screen size
        val metrics = DisplayMetrics()
        val manager = Global.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        manager.defaultDisplay.getMetrics(metrics)
        // make wrapper view fullscreen
        wrapper.layoutParams.width = metrics.widthPixels
        wrapper.layoutParams.height = metrics.heightPixels
        wrapper.layoutParams = wrapper.layoutParams
        // show toast
        Observable.just("")
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val toast = Toast(context)
                toast.view = root
                toast.duration = if (withLongDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
                toast.show()
            }
            .subscribe()
    }
}