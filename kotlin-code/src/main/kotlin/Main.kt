package com.code.kotlin

import kotlin.coroutines.*

suspend fun main() {
//    val completeContinuation = object : Continuation<Int> {
//        override val context = EmptyCoroutineContext
//        override fun resumeWith(result: Result<Int>) {
//            println(result.getOrNull())
//        }
//    }
//    val safeContinuation = suspend {
//        100
//    }.createCoroutine(completeContinuation)
//    safeContinuation.resume(Unit)
    val suspendCoroutine = suspendCoroutine<Int> { continuation ->
        continuation.resume(100)
    }
        println(suspendCoroutine)
    Thread.sleep(2000L)
}
