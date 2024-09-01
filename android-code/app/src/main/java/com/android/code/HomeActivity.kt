package com.android.code

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.code.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val job = Job()
        val scope = CoroutineScope(job)
        val dispatcher = Dispatchers.Default
        val option = CoroutineStart.LAZY
        val launchJob = scope.launch(dispatcher, option) {
            println("coroutine by launch")
        }
        launchJob.start()
    }
}