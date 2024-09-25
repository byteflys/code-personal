package com.code.kotlin

import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*

fun <P, R> create(
    block: suspend LuaCoroutineScope<P, R>.() -> Unit
): LuaCoroutine<P, R> {
    val coroutine = LuaCoroutineImpl<P, R>()
    coroutine.state = AtomicReference(LuaCoroutineState.CREATED())
    block.startCoroutine(coroutine, coroutine)
    return coroutine
}

interface LuaCoroutine<P, R> {
    fun resume(value: R): P
    fun completed(): Boolean
}

@RestrictsSuspension
interface LuaCoroutineScope<P, R> {
    suspend fun yield(value: P): R
}

internal sealed class LuaCoroutineState<P, R> {
    class CREATED<P, R> : LuaCoroutineState<P, R>()
    class SUSPENDED<P, R>(val continuation: Continuation<R>, val value: P) : LuaCoroutineState<P, R>()
    class RESUMED<P, R>(val value: R) : LuaCoroutineState<P, R>()
    class COMPLETED<P, R> : LuaCoroutineState<P, R>()
}

internal class LuaCoroutineImpl<P, R> :
    LuaCoroutine<P, R>, LuaCoroutineScope<P, R>, Continuation<Unit> {

    override val context = EmptyCoroutineContext

    internal lateinit var state: AtomicReference<LuaCoroutineState<P, R>>

    override suspend fun yield(value: P): R = suspendCoroutine { continuation ->
        state.getAndUpdate {
            when (it) {
                is LuaCoroutineState.CREATED<P, R>,
                is LuaCoroutineState.RESUMED<P, R> -> LuaCoroutineState.SUSPENDED(continuation, value)
                is LuaCoroutineState.SUSPENDED<P, R>,
                is LuaCoroutineState.COMPLETED<P, R> -> throw IllegalStateException()
            }
        }
    }

    override fun resume(value: R): P {
        val previous = state.getAndUpdate {
            when (it) {
                is LuaCoroutineState.SUSPENDED<P, R> -> LuaCoroutineState.RESUMED(value)
                is LuaCoroutineState.CREATED<P, R>,
                is LuaCoroutineState.RESUMED<P, R>,
                is LuaCoroutineState.COMPLETED<P, R> -> throw IllegalStateException()
            }
        }
        if (previous is LuaCoroutineState.SUSPENDED<P, R>) {
            previous.continuation.resume(value)
            return previous.value
        }
        throw IllegalStateException()
    }

    override fun resumeWith(result: Result<Unit>) {
        state.getAndUpdate { LuaCoroutineState.COMPLETED() }
        result.getOrThrow()
    }

    override fun completed() = state.get() is LuaCoroutineState.COMPLETED
}