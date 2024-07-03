package com.android.library.skinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.library.skinner.SkinnerKit.installSkinnerFactory

abstract class SkinnerCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSkinnerFactory(this)
        beforeCreate()
        super.onCreate(savedInstanceState)
        afterCreate(savedInstanceState)
        val contentView = createContentView(layoutInflater)
        setContentView(contentView)
        initContentView()
    }

    open protected fun beforeCreate() = Unit

    open protected fun afterCreate(savedInstanceState: Bundle?) = Unit

    open abstract protected fun createContentView(layoutInflater: LayoutInflater): View

    open abstract protected fun initContentView()

    // should be called in beforeCreate
    fun registerSkinnerProvider(provider: SkinnerProvider) {
        (layoutInflater.factory2 as? SkinnerInflaterFactory)?.let {
            it.registerSkinnerProvider(provider)
        }
    }

    fun reloadContentView() {
        val contentView = createContentView(layoutInflater)
        setContentView(contentView)
        initContentView()
    }
}