package x.coroutine

import kotlin.coroutines.CoroutineContext

internal class SymmetricCoroutineImpl<T>(
    context: CoroutineContext,
    block: suspend SymmetricCoroutine<T>.() -> Unit
) : SymmetricCoroutine<T> {

    internal var isMain = false

    internal val coroutine: CoroutineImpl<T, Unit>

    init {
        coroutine = CoroutineImpl(context) { block() }
    }

    override fun isMain() = isMain

    override fun getParameter(): T {
        return coroutine.parameter!!
    }

    override suspend fun startCoroutine(param: T) {
        coroutine.resume(param)
    }

    override tailrec suspend fun <R> transfer(other: SymmetricCoroutine<R>, param: R) {
        if (!isMain) {
            coroutine.yield(Unit)
            return
        }
        if (!other.isMain()) {
            val impl = other as SymmetricCoroutineImpl<R>
            val parameter = impl.coroutine.resume(param)
            transfer(parameter.coroutine, parameter.value)
        }
    }
}