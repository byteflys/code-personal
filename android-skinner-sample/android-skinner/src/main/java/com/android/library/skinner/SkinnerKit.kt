package com.android.library.skinner

import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.library.skinner.SkinnerValues.KEY_SKIN_MODE
import com.android.library.skinner.SkinnerValues.KEY_SKIN_NAME
import com.android.library.skinner.SkinnerValues.PREFIX_SKINNABLE
import com.android.library.skinner.SkinnerValues.RESOURCE_ID_INVALID
import com.android.library.skinner.SkinnerValues.SKIN_MODE_NIGHT
import com.android.library.skinner.SkinnerValues.SKIN_MODE_NONE
import com.android.library.skinner.provider.BasicAttributeSkinner
import com.tencent.mmkv.MMKV
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

object SkinnerKit {

    fun isResourceAvailable(resourceId: Int) = resourceId != RESOURCE_ID_INVALID

    fun isResourceSkinnable(resourceId: Int): Boolean {
        if (!isResourceAvailable(resourceId)) {
            return false
        }
        val resourceName = SkinnerResources.originResources.getResourceName(resourceId)
        return resourceName.startsWith(PREFIX_SKINNABLE, true)
    }

    fun hasSkinnableAttribute(view: View, attrs: AttributeSet, namespace: String, attribute: String): Boolean {
        val resourceId = getAttributeResourceId(view, attrs, namespace, attribute)
        return isResourceSkinnable(resourceId)
    }

    fun getAttributeResourceId(view: View, attrs: AttributeSet, namespace: String, attribute: String): Int {
        return attrs.getAttributeResourceValue(namespace, attribute, RESOURCE_ID_INVALID)
    }

    fun getSkinName(): String {
        return MMKV.defaultMMKV().getString(KEY_SKIN_NAME, "")!!
    }

    fun setSkinName(name: String) {
        MMKV.defaultMMKV().putString(KEY_SKIN_NAME, name)
    }

    fun getSkinMode(): String {
        return MMKV.defaultMMKV().getString(KEY_SKIN_MODE, "")!!
    }

    fun setSkinMode(mode: String) {
        MMKV.defaultMMKV().putString(KEY_SKIN_MODE, mode)
    }

    fun getSkinPackagePath(name: String): String {
        val root = SkinnerResources.context.filesDir.absolutePath
        return Path(root, "skin/skin_$name.apk").absolutePathString()
    }

    fun installSkinnerFactory(activity: AppCompatActivity) {
        val factory = SkinnerInflaterFactory(activity)
        factory.registerViewProvider(BasicAttributeSkinner)
        val field = LayoutInflater::class.java.getDeclaredField("mFactory2")
        field.isAccessible = true
        field.set(activity.layoutInflater, factory)
    }

    fun registerViewProvider(activity: AppCompatActivity, provider: SkinnerProvider) {
        (activity.layoutInflater.factory2 as? SkinnerInflaterFactory)?.let {
            it.registerViewProvider(provider)
        }
    }

    fun installSkin(inputStream: InputStream, name: String) {
        val path = getSkinPackagePath(name)
        val fos = FileOutputStream(path)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        fos.write(buffer)
        fos.flush()
        fos.close()
    }

    fun loadSkin(name: String) {
        val path = getSkinPackagePath(name)
        SkinnerResources.setHookedAssetManager(path)
    }

    fun setNightMode() = setSkinMode(SKIN_MODE_NIGHT)

    fun setDayMode() = setSkinMode(SKIN_MODE_NONE)

    fun uninstallSkinnerFactory(activity: AppCompatActivity) {

    }

    fun unloadSkin() {

    }
}