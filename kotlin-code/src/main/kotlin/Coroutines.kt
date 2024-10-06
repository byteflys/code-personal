package x.coroutine

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

sealed class Status {
    internal class Created(val continuation: Continuation<Unit>) : Status()
    internal class Suspended<P>(val continuation: Continuation<P>) : Status()
    internal class Resumed<R>(val continuation: Continuation<R>) : Status()
    internal data object Completed : Status()
}

fun <P, R> launch(
    context: CoroutineContext,
    block: suspend CoroutineScope<P, R>.() -> R
): Coroutineee<P, R> {
    return Coroutineee(context, block)
}