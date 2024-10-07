package x.coroutine

import kotlin.coroutines.CoroutineContext

internal class SymmetricCoroutineImpl<T>(
    context: CoroutineContext,
    block: suspend SymmetricCoroutine<T>.() -> Unit
) : SymmetricCoroutine<T> {

    internal val coroutine: CoroutineImpl<T, Unit>

    init {
        coroutine = CoroutineImpl(context) { block() }
    }

    override fun getParameter(): T {
        return coroutine.parameter!!
    }

    override suspend fun startCoroutine(param: T) {
        coroutine.resume(param)
    }

    override suspend fun <R> transfer(other: SymmetricCoroutine<R>, param: R) {
        (other as SymmetricCoroutineImpl<R>).coroutine.resume(param)
    }
}