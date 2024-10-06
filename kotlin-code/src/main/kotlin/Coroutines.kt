package x.coroutine

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface WriteableCoroutine<P, R> {

    var parameter: P?

    suspend fun yield(result: R): P
}

interface ReadableCoroutine<P, R> {

    suspend fun resume(parameter: P): R

    fun completed(): Boolean
}

interface CoroutineScope {
    var coroutineContext: CoroutineContext
}

sealed class Status {
    internal class Created(val continuation: Continuation<Unit>) : Status()
    internal class Suspended<P>(val continuation: Continuation<P>) : Status()
    internal class Resumed<R>(val continuation: Continuation<R>) : Status()
    internal data object Completed : Status()
}

object GlobalScope : CoroutineScope {
    override var coroutineContext: CoroutineContext = EmptyCoroutineContext
}

fun <P, R> GlobalScope.launch(
    context: CoroutineContext,
    block: suspend WriteableCoroutine<P, R>.() -> R
): ReadableCoroutine<P, R> {
    val context = coroutineContext + context
    return CoroutineImpl(context, block)
}

fun <P, R> GlobalScope.launch(
    block: suspend WriteableCoroutine<P, R>.() -> R
) = GlobalScope.launch(EmptyCoroutineContext, block)