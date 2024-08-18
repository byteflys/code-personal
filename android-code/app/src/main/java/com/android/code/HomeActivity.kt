package com.android.code

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.code.aop.AndroidContentViewPointcut
import com.android.code.databinding.ActivityHome2Binding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    @AndroidContentViewPointcut("HomeActivity")
    override fun setContentView(view: View) {
        super.setContentView(view)
    }

    private fun initView() {
        Log.e("HomeActivity", "initView")
    }
}