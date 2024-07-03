package com.android.app

import android.view.LayoutInflater
import android.view.View
import com.android.app.databinding.ActivityHomeBinding
import com.android.library.skinner.SkinnerCompatActivity
import com.android.library.skinner.SkinnerKit
import com.android.library.skinner.SkinnerValues

class HomeActivity : SkinnerCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun createContentView(layoutInflater: LayoutInflater): View {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initContentView() {
        binding.loadSkin.setOnClickListener { loadSkin() }
        binding.unloadSkin.setOnClickListener { unloadSkin() }
        binding.nightMode.setOnClickListener { nightMode() }
        binding.dayMode.setOnClickListener { dayMode() }
    }

    override fun beforeCreate() {
        SkinnerKit.init(application)
        SkinnerKit.installSkin(assets.open("skin.apk"), "test")
    }

    private fun loadSkin() {
        SkinnerKit.installSkinnerFactory(this)
        SkinnerKit.loadSkin("test")
        reload("Load Successfully")
    }

    private fun unloadSkin() {
        SkinnerKit.uninstallSkinnerFactory(this)
        SkinnerKit.unloadSkin()
        reload("Unload Successfully")
    }

    private fun nightMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_NIGHT)
        reload("Set Night Mode Successfully")
    }

    private fun dayMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_NONE)
        reload("Set Day Mode Successfully")
    }

    private fun reload(message: String) {
        reloadContentView()
        binding.text.text = message
    }
}