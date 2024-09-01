package com.code.kotlin

import kotlinx.coroutines.*

suspend fun main() {
    val job = Job()
    val scope = CoroutineScope(job)
    val dispatcher = Dispatchers.Default
    val option = CoroutineStart.LAZY
    val launchJob = scope.launch(dispatcher, option) {
        println("coroutine by launch")
    }
    launchJob.start()
    delay(99000L)
}