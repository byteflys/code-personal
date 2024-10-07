package x.coroutine

import kotlin.coroutines.EmptyCoroutineContext

interface SymmetricCoroutine<T> {

    fun getParameter(): T

    suspend fun startCoroutine(param: T)

    suspend fun <R> transfer(other: SymmetricCoroutine<R>, param: R)
}

fun <T> createSymmetric(
    block: suspend SymmetricCoroutine<T>.() -> Unit
): SymmetricCoroutine<T> = SymmetricCoroutineImpl(EmptyCoroutineContext, block)

suspend fun main() {
    lateinit var main: SymmetricCoroutine<Unit>
    lateinit var coroutine1: SymmetricCoroutine<String>
    lateinit var coroutine2: SymmetricCoroutine<String>
    lateinit var coroutine3: SymmetricCoroutine<String>
    coroutine1 = createSymmetric {
        transfer(coroutine3, "d")
        transfer(main, Unit)
    }
    coroutine2 = createSymmetric {
        transfer(coroutine1, "c")
    }
    coroutine3 = createSymmetric {
        transfer(coroutine2, "b")
        transfer(coroutine1, "f")
    }
    main = createSymmetric {
        transfer(coroutine3, "a")
    }
    main.startCoroutine(Unit)
}