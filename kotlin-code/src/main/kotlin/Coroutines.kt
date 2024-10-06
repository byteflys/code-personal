package x.coroutine

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object Coroutines {

    fun <P, R> launch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope<P, R>.() -> R
    ): Coroutine<P, R> {
        return Coroutine(context, block)
    }
}