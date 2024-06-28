package com.android.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.app.databinding.ActivityHomeBinding
import com.android.library.skinner.SkinnerAssetManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val drawable = SkinnerAssetManager.skinDrawable(R.drawable.icon_app)
        binding.image.setImageDrawable(drawable)
    }
}