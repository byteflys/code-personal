package com.code.kotlin

import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun main() {
    val result = suspendCoroutine<Int> { continuation ->
        thread {
            continuation.resume(100)
            Thread.sleep(5000L)
            println(2)
        }
    }
    println(1)
}
