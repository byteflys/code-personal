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
        val skinName = SkinnerKit.getSkinName()
        val skinMode = SkinnerKit.getSkinMode()
        binding.text.text = "$skinName/$skinMode"
        binding.loadSkinner.setOnClickListener { loadSkinner() }
        binding.loadDefault.setOnClickListener { loadDefault() }
        binding.nightMode.setOnClickListener { nightMode() }
        binding.defaultMode.setOnClickListener { defaultMode() }
    }

    // TODO
    //  if skin is same, not reload
    //  if apk not exist, use origin resource
    override fun beforeCreate() {
        SkinnerKit.init(application)
        SkinnerKit.installSkinnerFactory(this)
        SkinnerKit.installSkin(assets.open("skin.apk"), "skinner")
    }

    private fun loadSkinner() {
        SkinnerKit.loadSkin("skinner")
        reloadContentView()
    }

    private fun loadDefault() {
        SkinnerKit.loadSkin(SkinnerValues.SKIN_NAME_DEFAULT)
        reloadContentView()
    }

    private fun nightMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_DARK)
        reloadContentView()
    }

    private fun defaultMode() {
        SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_DEFAULT)
        reloadContentView()
    }
}