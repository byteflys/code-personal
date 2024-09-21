package com.code.kotlin

import kotlinx.coroutines.delay
import kotlin.coroutines.*

//fun main() {
//    val completeContinuation = object : Continuation<Int> {
//        override val context = EmptyCoroutineContext
//        override fun resumeWith(result: Result<Int>) {
//            println(result.getOrThrow()) // ④
//        }
//    }
//    val safeContinuation = suspend {
//        val x = 100 // ③
//        delay(200L)
//        val y = x + 200
//        delay(300L)
//        val z = y + 300
//        z
//    }.createCoroutine(completeContinuation) // ①
//    safeContinuation.resume(Unit) // ②
//    Thread.sleep(99999999999999L)
//}

suspend fun main() {
    val result = suspendCoroutine<Int> { continuation ->
//        continuation.resume(100)
    }
    println(result)
    Thread.sleep(99999L)
}
