package com.android.library.skinner

import android.app.Application
import android.content.res.AssetManager
import android.content.res.Resources

@Suppress("Deprecated")
object SkinnerAssetManager {

    private lateinit var context: Application

    lateinit var assetManager: AssetManager
    lateinit var resources: Resources

    fun init(application: Application, resourcePath: String) = apply {
        context = application
        createHookedAssetManager(resourcePath)
    }

    private fun createHookedAssetManager(resourcePath: String) {
        val assetManager = AssetManager::class.java.newInstance()
        val method = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
        method.invoke(assetManager, resourcePath)
        val resources = Resources(assetManager, context.resources.displayMetrics, context.resources.configuration)
        this.assetManager = assetManager
        this.resources = resources
    }
}