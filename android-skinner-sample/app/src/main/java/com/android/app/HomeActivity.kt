package com.android.app

import android.view.LayoutInflater
import android.view.View
import com.android.app.databinding.ActivityHomeBinding
import com.android.library.skinner.SkinnerCompatActivity
import com.android.library.skinner.SkinnerKit
import com.android.library.skinner.SkinnerValues

class HomeActivity : SkinnerCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun beforeCreate() {
        initSkinner()
        installSkinner()
        val lastSkin = SkinnerKit.getSkinName()
        SkinnerKit.loadSkin(lastSkin)
    }

    override fun createContentView(layoutInflater: LayoutInflater): View {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initContentView() {
        val skinName = SkinnerKit.getSkinName()
        val skinMode = SkinnerKit.getSkinMode()
        binding.text.text = "$skinName/$skinMode"
        binding.loadSkinner.setOnClickListener { loadSkinnerSkin() }
        binding.loadDefault.setOnClickListener { loadDefaultSkin() }
        binding.nightMode.setOnClickListener { setDarkMode() }
        binding.defaultMode.setOnClickListener { setDefaultMode() }
    }

    private fun initSkinner() {
        SkinnerKit.init(application)
        SkinnerKit.installSkin(assets.open("skin.apk"), "skinner")
    }

    private fun installSkinner() {
        SkinnerKit.installSkinnerFactory(this)
    }

    private fun uninstallSkinner() {
        SkinnerKit.uninstallSkinnerFactory(this)
        reloadContentView()
    }

    private fun loadSkinnerSkin() {
        SkinnerKit.loadSkin("skinner22")
        reloadContentView()
    }

    private fun loadDefaultSkin() {
        SkinnerKit.loadSkin(SkinnerValues.SKIN_NAME_DEFAULT)
        reloadContentView()
    }

    private fun setDarkMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_DARK)
        reloadContentView()
    }

    private fun setDefaultMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_DEFAULT)
        reloadContentView()
    }
}