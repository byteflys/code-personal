package com.android.library.skinner

import android.app.Application
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable

@Suppress("Deprecated")
object SkinnerAssetManager {

    lateinit var context: Application
    lateinit var assetManager: AssetManager
    lateinit var resources: Resources
    lateinit var originResources: Resources

    fun init(application: Application, resourcePath: String) = apply {
        context = application
        createHookedAssetManager(resourcePath)
    }

    private fun createHookedAssetManager(resourcePath: String) {
        val assetManager = AssetManager::class.java.newInstance()
        val method = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
        method.invoke(assetManager, resourcePath)
        this.originResources = context.resources
        val resources = Resources(assetManager, originResources.displayMetrics, originResources.configuration)
        this.assetManager = assetManager
        this.resources = resources
    }

    fun resourceType(resId: Int): String {
        return originResources.getResourceTypeName(resId)
    }

    fun skinRes(resId: Int): Int {
        return resources.getIdentifier(
            originResources.getResourceName(resId),
            originResources.getResourceTypeName(resId),
            originResources.getResourcePackageName(resId)
        )
    }

    fun skinColor(resId: Int): Int {
        val skinResId = skinRes(resId)
        if (skinResId > 0) {
            return resources.getColor(skinResId)
        }
        return originResources.getColor(resId)
    }

    fun skinDrawable(resId: Int): Drawable {
        val skinResId = skinRes(resId)
        if (skinResId > 0) {
            return resources.getDrawable(skinResId)
        }
        return originResources.getDrawable(resId)
    }
}