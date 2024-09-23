package com.code.kotlin

import kotlin.coroutines.*

interface AsyncScope<T> {
    fun getContinuation(): Continuation<T>
    fun setContinuation(continuation: Continuation<T>)
    fun resume(value: T)
    fun error(e: Throwable)
}

class AsyncScopeImpl<T> : AsyncScope<T>, Continuation<Unit> {

    override val context = EmptyCoroutineContext

    private lateinit var continuation: Continuation<T>

    override fun getContinuation() = continuation

    override fun setContinuation(continuation: Continuation<T>) {
        this.continuation = continuation
    }

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
    block: suspend AsyncScope<T>.() -> Unit
) {
    val asyncScope = AsyncScopeImpl<T>()
    block.startCoroutine(asyncScope, asyncScope)
}

suspend fun <T> AsyncScope<T>.await(
    block: AsyncScope<T>.() -> Unit
) = suspendCoroutine { continuation ->
    setContinuation(continuation)
    block()
}