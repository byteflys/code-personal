package com.code.kotlin

import kotlin.coroutines.*

fun <IN, OUT> create(
    block: suspend LuaCoroutineScope<IN>.() -> OUT
): LuaCoroutine<IN> {
    val generator = GeneratorImpl<T>()
    val continuation = block.createCoroutine(generator, generator)
    generator.state = GeneratorState.WAITING(continuation)
    return generator
}

interface LuaCoroutine<IN> {
    fun resume(value: IN)
}

@RestrictsSuspension
interface LuaCoroutineScope<OUT> {
    suspend fun yield(value: OUT)
}

internal class LuaCoroutineImpl<IN, OUT> :
    LuaCoroutine<IN>, LuaCoroutineScope<OUT>, Continuation<OUT> {

    override val context = EmptyCoroutineContext

    override fun resume(value: IN) {
        TODO("Not yet implemented")
    }

    override suspend fun yield(value: OUT) {
        TODO("Not yet implemented")
    }

    override fun resumeWith(result: Result<OUT>) {
        result.getOrThrow()
    }
}