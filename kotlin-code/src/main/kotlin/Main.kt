package com.code.kotlin

import kotlin.coroutines.*

fun main() {
    val completeContinuation = object : Continuation<Int> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Int>) {
            println(result.getOrThrow()) // ③
        }
    }
    val safeContinuation = suspend {
        100 // ②
    }.createCoroutine(completeContinuation)
    safeContinuation.resume(Unit) // ①
}

//suspend fun main() {
//    val result = suspendCoroutine { continuation ->
//        continuation.resume(100) // ①
//    }
//    println(result) // ②
//}
