package com.android.code

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.code.databinding.ActivityHomeBinding
import com.blankj.utilcode.util.LanguageUtils
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        val sysLang = LanguageUtils.getSystemLanguage()
        val lang = sysLang.language
        val langTag = Locale.getDefault().toLanguageTag()
        println("Language $lang $langTag")
    }
}