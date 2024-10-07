package x.coroutine

import kotlin.coroutines.CoroutineContext

internal class SymmetricCoroutineImpl<T>(
    context: CoroutineContext,
    block: suspend SymmetricCoroutineScope<T>.() -> Unit
) : SymmetricCoroutine<T>, SymmetricCoroutineScope<T> {

    internal var isMain = false

    internal val coroutine: CoroutineImpl<T, TransferContext<*>?> = CoroutineImpl(context) {
        block()
        return@CoroutineImpl null
    }

    override fun isMain() = isMain

    override fun getParameter(): T {
        return coroutine.parameter!!
    }

    override suspend fun <R> transfer(other: SymmetricCoroutine<R>, param: R) = transferInner(other, param)

    private tailrec suspend fun <R> transferInner(other: SymmetricCoroutine<R>, param: Any?) {
        if (!isMain) {
            val transferContext = TransferContext(other, param as R)
            coroutine.yield(transferContext)
            return
        }
        if (!other.isMain()) {
            val impl = other as SymmetricCoroutineImpl<R>
            val transferContext = impl.coroutine.resume(param as R)
            transferContext?.let {
                transferInner(it.coroutine, it.parameter)
            }
        }
    }

    override suspend fun clean() {
        while (!coroutine.completed()) {
            coroutine.resume(getParameter())
        }
    }
}