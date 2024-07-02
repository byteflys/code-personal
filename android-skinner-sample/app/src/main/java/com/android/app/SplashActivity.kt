package com.android.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.library.skinner.SkinnerAssetManager
import com.tencent.mmkv.MMKV
import java.io.FileOutputStream

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Global.application = application
        MMKV.initialize(application)
        copySkinPackage()
        SkinnerAssetManager.init(application, "sdcard/skin.apk")
    }

    private fun copySkinPackage() {
        val fis = application.assets.open("skin.apk")
        val fos = FileOutputStream("sdcard/skin.apk")
        val buffer = ByteArray(fis.available())
        fis.read(buffer)
        fos.write(buffer)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()
        if (!Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:$packageName")
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            intent.data = uri
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}