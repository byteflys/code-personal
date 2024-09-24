package com.code.kotlin

import kotlin.coroutines.*

fun <PARAM, RESULT> create(
    block: suspend LuaCoroutineScope<PARAM, RESULT>.() -> Unit
): LuaCoroutine<PARAM, RESULT> {
    val coroutine = LuaCoroutineImpl<PARAM, RESULT>()
    val continuation = block.createCoroutine(coroutine, coroutine)
    coroutine.state = CoroutineState.WAITING(continuation)
    return coroutine
}

interface LuaCoroutine<PARAM, RESULT> {
    fun resume(value: PARAM): RESULT
}

@RestrictsSuspension
interface LuaCoroutineScope<PARAM, RESULT> {
    suspend fun yield(value: RESULT): PARAM
}

internal sealed class CoroutineState<RESULT> {
    class WAITING<RESULT>(val continuation: Continuation<Unit>) : CoroutineState<RESULT>()
    class READY<RESULT>(val continuation: Continuation<Unit>, val value: RESULT) : CoroutineState<RESULT>()
    class COMPLETED<RESULT> : CoroutineState<RESULT>()
}

internal class LuaCoroutineImpl<PARAM, RESULT> :
    LuaCoroutine<PARAM, RESULT>, LuaCoroutineScope<PARAM, RESULT>, Continuation<Unit> {

    override val context = EmptyCoroutineContext

    internal lateinit var state: CoroutineState<RESULT>

    override suspend fun yield(value: RESULT): PARAM {
        TODO("Not yet implemented")
    }

    override fun resume(value: PARAM): RESULT {
        TODO("Not yet implemented")
    }

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow()
    }
}