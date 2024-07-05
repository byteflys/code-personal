package com.android.app

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.android.app.databinding.ActivityHomeBinding
import com.android.library.skinner.SkinnerCompatActivity
import com.android.library.skinner.SkinnerKit
import com.android.library.skinner.SkinnerValues

class HomeActivity : SkinnerCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var root: View

    override fun createContentView(layoutInflater: LayoutInflater): View {
        root = layoutInflater.inflate(R.layout.activity_home, null)
        return root
    }

    override fun initContentView() {
        val skinName = SkinnerKit.getSkinName()
        val skinMode = SkinnerKit.getSkinMode()
        root.findViewById<TextView>(R.id.text).text = "$skinName/$skinMode"
        root.findViewById<View>(R.id.loadSkinner).setOnClickListener { loadSkinner() }
        root.findViewById<View>(R.id.loadDefault).setOnClickListener { loadDefault() }
        root.findViewById<View>(R.id.nightMode).setOnClickListener { nightMode() }
        root.findViewById<View>(R.id.dayMode).setOnClickListener { dayMode() }
    }

    override fun beforeCreate() {
        SkinnerKit.init(application)
        SkinnerKit.installSkin(assets.open("skin.apk"), "skinner")
    }

    private fun loadSkinner() {
        SkinnerKit.installSkinnerFactory(this)
        SkinnerKit.loadSkin("skinner")
        reloadContentView()
    }

    private fun loadDefault() {
        SkinnerKit.installSkinnerFactory(this)
        SkinnerKit.loadSkin(SkinnerValues.SKIN_NAME_DEFAULT)
        reloadContentView()
    }

    private fun nightMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_NIGHT)
        reloadContentView()
    }

    private fun dayMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_DEFAULT)
        reloadContentView()
    }
}