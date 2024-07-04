package com.android.library.skinner

import android.app.Application
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.library.skinner.SkinnerValues.KEY_SKIN_MODE
import com.android.library.skinner.SkinnerValues.KEY_SKIN_NAME
import com.android.library.skinner.SkinnerValues.SUFFIX_SKINNABLE
import com.android.library.skinner.SkinnerValues.RESOURCE_ID_INVALID
import com.android.library.skinner.SkinnerValues.SKIN_MODE_DEFAULT
import com.android.library.skinner.SkinnerValues.SKIN_NAME_DEFAULT
import com.tencent.mmkv.MMKV
import java.io.File
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
        return resourceName.endsWith(SUFFIX_SKINNABLE, true)
    }

    fun hasSkinnableAttribute(view: View, attrs: AttributeSet, namespace: String, attribute: String): Boolean {
        val resourceId = getAttributeResourceId(view, attrs, namespace, attribute)
        return isResourceSkinnable(resourceId)
    }

    fun getAttributeResourceId(view: View, attrs: AttributeSet, namespace: String, attribute: String): Int {
        return attrs.getAttributeResourceValue(namespace, attribute, RESOURCE_ID_INVALID)
    }

    fun getSkinName(): String {
        return MMKV.defaultMMKV().getString(KEY_SKIN_NAME, SKIN_NAME_DEFAULT)!!
    }

    fun setSkinName(name: String) {
        MMKV.defaultMMKV().putString(KEY_SKIN_NAME, name)
    }

    fun getSkinMode(): String {
        return MMKV.defaultMMKV().getString(KEY_SKIN_MODE, SKIN_MODE_DEFAULT)!!
    }

    fun setSkinMode(mode: String) {
        MMKV.defaultMMKV().putString(KEY_SKIN_MODE, mode)
    }

    fun getSkinPackagePath(name: String): String {
        val root = SkinnerResources.context.filesDir.absolutePath
        return Path(root, "skin/skin_$name.apk").absolutePathString()
    }

    fun init(application: Application) {
        SkinnerResources.context = application
        MMKV.initialize(application)
    }

    fun installSkinnerFactory(activity: AppCompatActivity) {
        val factory = SkinnerInflaterFactory(activity)
        val field2 = LayoutInflater::class.java.getDeclaredField("mFactory2")
        field2.isAccessible = true
        field2.set(activity.layoutInflater, factory)
        val field1 = LayoutInflater::class.java.getDeclaredField("mFactory")
        field1.isAccessible = true
        field1.set(activity.layoutInflater, factory)
    }

    fun registerViewProvider(activity: AppCompatActivity, provider: SkinnerProvider) {
        (activity.layoutInflater.factory2 as? SkinnerInflaterFactory)?.let {
            it.registerViewProvider(provider)
        }
    }

    fun installSkin(inputStream: InputStream, name: String) {
        val path = getSkinPackagePath(name)
        File(path).apply {
            parentFile.mkdirs()
            createNewFile()
        }
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
        setSkinName(name)
    }

    fun uninstallSkinnerFactory(activity: AppCompatActivity) {
        val field = LayoutInflater::class.java.getDeclaredField("mFactory2")
        field.isAccessible = true
        field.set(activity.layoutInflater, activity.delegate)
        setSkinName(SKIN_NAME_DEFAULT)
    }
}