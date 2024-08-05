package com.android.code

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.android.code.databinding.ActivityHome2Binding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.button.setOnClickListener {
            binding.editText.requestFocus()
            val imm = getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}