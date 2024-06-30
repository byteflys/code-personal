package com.android.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutInflater.factory2 = SkinnerInflaterFactory(this)
        super.onCreate(savedInstanceState)
        val root = layoutInflater.inflate(R.layout.activity_home, null)
        setContentView(root)
    }
}