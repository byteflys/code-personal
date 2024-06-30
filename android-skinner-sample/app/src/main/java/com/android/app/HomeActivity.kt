package com.android.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setSkinnerInflaterFactory( this)
        super.onCreate(savedInstanceState)
        val root = layoutInflater.inflate(R.layout.activity_home, null)
        setContentView(root)
    }

    private fun setSkinnerInflaterFactory(  activity: AppCompatActivity) {
        activity.layoutInflater.factory2 = SkinnerInflaterFactory(activity)
    }
}