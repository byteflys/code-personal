package com.code.kotlin

import kotlin.coroutines.*

fun <R, P> create(
    block: suspend LuaCoroutineScope<R, P>.() -> Unit
): LuaCoroutine<R, P> {
    val coroutine = LuaCoroutineImpl<R, P>()
    coroutine.state = LuaCoroutineState.CREATED()
    block.startCoroutine(coroutine, coroutine)
    return coroutine
}

interface LuaCoroutine<R, P> {
    fun resume(value: R): P
}

@RestrictsSuspension
interface LuaCoroutineScope<R, P> {
    suspend fun yield(value: P): R
}

internal sealed class LuaCoroutineState<R, P> {
    class CREATED<R, P> : LuaCoroutineState<R, P>()
    class SUSPENDED<R, P>(val continuation: Continuation<R>, val value: P) : LuaCoroutineState<R, P>()
    class RESUMED<R, P>(val value: R) : LuaCoroutineState<R, P>()
    class COMPLETED<R, P> : LuaCoroutineState<R, P>()
}

internal class LuaCoroutineImpl<R, P> :
    LuaCoroutine<R, P>, LuaCoroutineScope<R, P>, Continuation<Unit> {

    override val context = EmptyCoroutineContext

    internal lateinit var state: LuaCoroutineState<R, P>

    override suspend fun yield(value: P): R = suspendCoroutine { continuation ->
        when (val old = state) {
            is LuaCoroutineState.CREATED<*, *>,
            is LuaCoroutineState.RESUMED<*, *> -> {
                state = LuaCoroutineState.SUSPENDED(continuation, value)
            }
            is LuaCoroutineState.SUSPENDED<*, *>,
            is LuaCoroutineState.COMPLETED<*, *> -> throw IllegalStateException()
        }
    }

    override fun resume(value: R): P {
        when (val old = state) {
            is LuaCoroutineState.SUSPENDED<R, P> -> {
                state = LuaCoroutineState.RESUMED(value)
                old.continuation.resume(value)
                return old.value
            }
            is LuaCoroutineState.CREATED<R, P>,
            is LuaCoroutineState.RESUMED<R, P>,
            is LuaCoroutineState.COMPLETED<R, P> -> throw IllegalStateException()
        }
    }

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow()
    }
}