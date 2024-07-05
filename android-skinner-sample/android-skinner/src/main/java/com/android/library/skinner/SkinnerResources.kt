package com.android.library.skinner

import android.app.Application
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable

object SkinnerResources {

    lateinit var context: Application
    lateinit var assetManager: AssetManager
    lateinit var resources: Resources
    lateinit var originResources: Resources

    fun setHookedAssetManager(resourcePath: String) {
        val assetManager = AssetManager::class.java.newInstance()
        val method = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
        method.invoke(assetManager, resourcePath)
        this.originResources = context.resources
        val resources = Resources(assetManager, originResources.displayMetrics, originResources.configuration)
        this.assetManager = assetManager
        this.resources = resources
    }

    fun isResourceAvailable(resourceId: Int) = resourceId != SkinnerValues.RESOURCE_ID_INVALID

    fun resourceType(resId: Int): String {
        return originResources.getResourceTypeName(resId)
    }

    fun skinRes(resources: Resources, resId: Int): Int {
        return resources.getIdentifier(
            SkinnerKit.getSkinnableResourceName(resId),
            originResources.getResourceTypeName(resId),
            originResources.getResourcePackageName(resId)
        )
    }

    fun skinColor(resId: Int): Int {
        val skinResId = skinRes(resources, resId)
        if (skinResId > 0) {
            return resources.getColor(skinResId)
        }
        val originResIdWithMode = skinRes(originResources, resId)
        if (originResIdWithMode > 0) {
            return originResources.getColor(originResIdWithMode)
        }
        return originResources.getColor(resId)
    }

    fun skinDrawable(resId: Int): Drawable {
        val skinResId = skinRes(resources, resId)
        if (skinResId > 0) {
            return resources.getDrawable(skinResId)
        }
        val originResIdWithMode = skinRes(originResources, resId)
        if (originResIdWithMode > 0) {
            return originResources.getDrawable(originResIdWithMode)
        }
        return originResources.getDrawable(resId)
    }
}