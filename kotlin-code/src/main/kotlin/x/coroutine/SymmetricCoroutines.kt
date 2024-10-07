package x.coroutine

import kotlin.coroutines.EmptyCoroutineContext

interface SymmetricCoroutine<T> {

    fun isMain(): Boolean

    fun getParameter(): T

    suspend fun <R> transfer(other: SymmetricCoroutine<R>, param: R)
}

data class TransferContext<T>(
    val coroutine: SymmetricCoroutine<T>,
    val parameter: T?
)

fun <T> createSymmetric(
    block: suspend SymmetricCoroutine<T>.() -> Unit
): SymmetricCoroutine<T> {
    return SymmetricCoroutineImpl(EmptyCoroutineContext, block)
}

suspend fun <T> launchSymmetric(symmetric: SymmetricCoroutine<T>, param: T) {
    val main = SymmetricCoroutineImpl<Unit>(EmptyCoroutineContext) {
        transfer(symmetric, param)
    }
    main.isMain = true
    main.coroutine.resume(Unit)
}