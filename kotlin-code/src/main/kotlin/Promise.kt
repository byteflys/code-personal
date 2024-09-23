package com.code.kotlin

import kotlin.coroutines.*

interface Promise<T> {
    fun resume(value: T)
    fun error(e: Throwable)
}

internal class PromiseImpl<T> : Promise<T>, Continuation<Unit> {

    override val context = EmptyCoroutineContext

    internal lateinit var continuation: Continuation<T>

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow()
    }

    override fun resume(value: T) {
        continuation.resume(value)
    }

    override fun error(e: Throwable) {
        continuation.resumeWithException(e)
    }
}

fun <T> async(
    block: suspend Promise<T>.() -> Unit
): Promise<T> {
    val promise = PromiseImpl<T>()
    block.startCoroutine(promise, promise)
    return promise
}

suspend fun <T> Promise<T>.await(
    block: Promise<T>.() -> Unit
) = suspendCoroutine { continuation ->
    val impl = this as PromiseImpl<T>
    impl.continuation = continuation
    impl.block()
}