package com.android.app

import android.view.LayoutInflater
import android.view.View
import com.android.app.databinding.ActivityHomeBinding
import com.android.library.skinner.SkinnerCompatActivity

class HomeActivity : SkinnerCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun createContentView(layoutInflater: LayoutInflater): View {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initContentView() {
        binding.installPlugin.setOnClickListener { installPlugin() }
        binding.uninstallPlugin.setOnClickListener { uninstallPlugin() }
        binding.loadSkin.setOnClickListener { loadSkin() }
        binding.unloadSkin.setOnClickListener { unloadSkin() }
        binding.nightMode.setOnClickListener { nightMode() }
        binding.dayMode.setOnClickListener { dayMode() }
    }

    private fun installPlugin() {

    }

    private fun uninstallPlugin() {

    }

    private fun loadSkin() {

    }

    private fun unloadSkin() {

    }

    private fun nightMode() {

    }

    private fun dayMode() {

    }
}