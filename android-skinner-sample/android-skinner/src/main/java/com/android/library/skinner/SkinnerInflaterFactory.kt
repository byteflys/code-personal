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
        val view = activity.delegate.createView(parent, name, context, attrs)
        view ?: return null
        providers.forEach {
            if (it.isProviderSupported(view, attrs))
                it.hookView(view, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet) = null
}