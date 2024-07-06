package com.android.library.skinner

import android.app.Application
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.library.skinner.SkinnerResources.isResourceAvailable
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
        if (name == SKIN_NAME_DEFAULT) {
            return SkinnerResources.context.packageResourcePath
        }
        val root = SkinnerResources.context.filesDir.absolutePath
        return Path(root, "skin/skin_$name.apk").absolutePathString()
    }

    fun getSkinnableResourceName(resId: Int): String {
        val mode = getSkinMode()
        var name = SkinnerResources.originResources.getResourceName(resId)
        if (mode != SKIN_MODE_DEFAULT) {
            name = name + "_" + mode
        }
        return name
    }

    fun init(application: Application) {
        MMKV.initialize(application)
        if (SkinnerResources.resources == null) {
            SkinnerResources.context = application
            SkinnerResources.assetManager = application.assets
            SkinnerResources.resources = application.resources
            SkinnerResources.originResources = application.resources
        }
    }

    fun installSkinnerFactory(activity: AppCompatActivity) {
        val factory = SkinnerInflaterFactory(activity)
        val field1 = LayoutInflater::class.java.getDeclaredField("mFactory")
        field1.isAccessible = true
        field1.set(activity.layoutInflater, factory)
        val field2 = LayoutInflater::class.java.getDeclaredField("mFactory2")
        field2.isAccessible = true
        field2.set(activity.layoutInflater, factory)
        val lastSkin = getSkinName()
        loadSkin(lastSkin)
    }

    fun uninstallSkinnerFactory(activity: AppCompatActivity) {
        val field1 = LayoutInflater::class.java.getDeclaredField("mFactory")
        field1.isAccessible = true
        field1.set(activity.layoutInflater, activity.delegate)
        val field2 = LayoutInflater::class.java.getDeclaredField("mFactory2")
        field2.isAccessible = true
        field2.set(activity.layoutInflater, activity.delegate)
        setSkinName(SKIN_NAME_DEFAULT)
        setSkinMode(SKIN_MODE_DEFAULT)
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
}