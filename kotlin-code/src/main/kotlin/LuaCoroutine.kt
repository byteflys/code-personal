package com.code.kotlin

import kotlin.coroutines.*

fun <PARAM, RESULT> create(
    block: suspend LuaCoroutineScope<PARAM, RESULT>.() -> Unit
): LuaCoroutine<PARAM, RESULT> {
    val coroutine = LuaCoroutineImpl<PARAM, RESULT>()
    val continuation = block.createCoroutine(coroutine, coroutine)
    coroutine.state = LuaCoroutineState.CREATED(continuation)
    continuation.resume(Unit)
    return coroutine
}

interface LuaCoroutine<PARAM, RESULT> {
    fun resume(value: PARAM): RESULT
}

@RestrictsSuspension
interface LuaCoroutineScope<PARAM, RESULT> {
    suspend fun yield(value: RESULT): PARAM
}

internal sealed class LuaCoroutineState<PARAM, RESULT> {
    class CREATED<PARAM, RESULT>(val continuation: Continuation<Unit>) : LuaCoroutineState<PARAM, RESULT>()
    class SUSPENDED<PARAM, RESULT>(val continuation: Continuation<PARAM>, val value: RESULT) : LuaCoroutineState<PARAM, RESULT>()
    class RESUMED<PARAM, RESULT>(val value: PARAM) : LuaCoroutineState<PARAM, RESULT>()
    class COMPLETED<PARAM, RESULT> : LuaCoroutineState<PARAM, RESULT>()
}

internal class LuaCoroutineImpl<PARAM, RESULT> :
    LuaCoroutine<PARAM, RESULT>, LuaCoroutineScope<PARAM, RESULT>, Continuation<Unit> {

    override val context = EmptyCoroutineContext

    internal lateinit var state: LuaCoroutineState<PARAM, RESULT>

    override suspend fun yield(value: RESULT): PARAM = suspendCoroutine { continuation ->
        when (val old = state) {
            is LuaCoroutineState.CREATED<*, *>,
            is LuaCoroutineState.RESUMED<*, *> -> {
                state = LuaCoroutineState.SUSPENDED(continuation, value)
            }
            is LuaCoroutineState.SUSPENDED<*, *>,
            is LuaCoroutineState.COMPLETED<*, *> -> throw IllegalStateException()
        }
    }

    override fun resume(value: PARAM): RESULT {
        when (val old = state) {
            is LuaCoroutineState.SUSPENDED<PARAM, RESULT> -> {
                state = LuaCoroutineState.RESUMED(value)
                old.continuation.resume(value)
                return old.value
            }
            is LuaCoroutineState.CREATED<PARAM, RESULT>,
            is LuaCoroutineState.RESUMED<PARAM, RESULT>,
            is LuaCoroutineState.COMPLETED<PARAM, RESULT> -> throw IllegalStateException()
        }
    }

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow()
    }
}