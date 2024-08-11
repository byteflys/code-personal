package com.android.code

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.code.databinding.ActivityHome2Binding
import com.android.code.ui.FlowLayoutManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = FlowLayoutManager()
        binding.recyclerView.adapter = SimpleAdapter()
    }
}