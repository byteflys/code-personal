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

    }
}