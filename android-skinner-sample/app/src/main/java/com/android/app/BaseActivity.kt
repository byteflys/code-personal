package com.android.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.library.skinner.SkinnerInflaterFactory
import com.android.library.skinner.SkinnerProvider
import com.android.library.skinner.provider.BasicAttributeSkinner

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSkinnerFactory()
        super.onCreate(savedInstanceState)
        val contentView = createContentView()
        setContentView(contentView)
        initContentView()
    }

    private fun installSkinnerFactory() {
        val factory = SkinnerInflaterFactory(this)
        factory.registerSkinnerProvider(BasicAttributeSkinner)
        layoutInflater.factory2 = factory
    }

    open abstract protected fun createContentView(): View

    open abstract protected fun initContentView()

    fun registerSkinnerProvider(provider: SkinnerProvider) {
        (layoutInflater.factory2 as? SkinnerInflaterFactory)?.let {
            it.registerSkinnerProvider(provider)
        }
    }

    fun loadSkin(name: String) {
        val contentView = createContentView()
        setContentView(contentView)
        initContentView()
    }
}