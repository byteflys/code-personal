package com.android.library.skinner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

typealias androidStyleableRes = androidx.appcompat.R.styleable

class SkinnerInflaterFactory(private val activity: AppCompatActivity) : LayoutInflater.Factory2 {

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        val view = activity.delegate.createView(parent, name, context, attrs)
        if (view is ImageView) {
            skinImageView(view, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet) = null

    private fun skinImageView(view: ImageView, attrs: AttributeSet) {
        val typedArray = activity.obtainStyledAttributes(attrs, androidStyleableRes.AppCompatImageView)
        if (typedArray.hasValue(androidStyleableRes.AppCompatImageView_android_src)) {
            val srcDrawableId = typedArray.getResourceId(androidStyleableRes.AppCompatImageView_android_src, 0)
            val skinDrawable = SkinnerAssetManager.skinDrawable(srcDrawableId)
            view.setImageDrawable(skinDrawable)
        }
    }
}