package com.android.library.skinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class SkinnerCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeCreate()
        super.onCreate(savedInstanceState)
        afterCreate(savedInstanceState)
        reloadContentView()
    }

    open protected fun beforeCreate() = Unit

    open protected fun afterCreate(savedInstanceState: Bundle?) = Unit

    open abstract protected fun createContentView(layoutInflater: LayoutInflater): View

    open abstract protected fun initContentView()

    protected fun reloadContentView() {
        val contentView = createContentView(layoutInflater)
        setContentView(contentView)
        initContentView()
    }
}