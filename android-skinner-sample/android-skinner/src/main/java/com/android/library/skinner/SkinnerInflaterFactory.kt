package com.android.library.skinner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SkinnerInflaterFactory(private val activity: AppCompatActivity) : LayoutInflater.Factory2 {

    private val providers = mutableListOf<SkinnerProvider>()

    fun registerViewProvider(provider: SkinnerProvider) = apply {
        providers.add(provider)
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        val delegateView = activity.delegate.createView(parent, name, context, attrs)
        val inflateView = delegateView ?: activity.layoutInflater.onCreateView(context, parent, name, attrs)
        inflateView ?: return null
        providers.forEach {
            if (it.isProviderSupported(inflateView, attrs))
                it.hookView(inflateView, attrs)
        }
        return inflateView
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet) = null
}