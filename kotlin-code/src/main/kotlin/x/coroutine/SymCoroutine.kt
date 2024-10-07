package x.coroutine

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface SymCoroutineScope<T> {
    suspend fun <P> transfer(symCoroutine: SymCoroutine<P>, value: P): T
}

class SymCoroutine<T>(
    override val context: CoroutineContext = EmptyCoroutineContext,
    private val block: suspend SymCoroutineScope<T>.() -> Unit
) : Continuation<T> {

    companion object {
        lateinit var main: SymCoroutine<Any?>

        suspend fun main(block: suspend SymCoroutineScope<Any?>.() -> Unit) {
            SymCoroutine<Any?> { block() }.also { main = it }.start(Unit)
        }

        fun <T> create(
            context: CoroutineContext = EmptyCoroutineContext,
            block: suspend SymCoroutineScope<T>.() -> Unit
        ): SymCoroutine<T> {
            return SymCoroutine(context, block)
        }
    }

    class Parameter<T>(val coroutine: SymCoroutine<T>, val value: T)

    val isMain: Boolean
        get() = this == main

    private val body: SymCoroutineScope<T> = object : SymCoroutineScope<T> {
        private tailrec suspend fun <P> transferInner(symCoroutine: SymCoroutine<P>, value: Any?): T {
            if (this@SymCoroutine.isMain) {
                return if (symCoroutine.isMain) {
                    value as T
                } else {
                    val parameter = symCoroutine.coroutine.resume(value as P)
                    transferInner(parameter.coroutine, parameter.value)
                }
            } else {
                coroutine.run {
                    return yield(Parameter(symCoroutine, value as P))
                }
            }
        }

        override suspend fun <P> transfer(symCoroutine: SymCoroutine<P>, value: P): T {
            return transferInner(symCoroutine, value)
        }
    }

    private val coroutine = CoroutineImpl<T, Parameter<*>>(context) {
        Parameter(this@SymCoroutine, suspend {
            block(body)
            if (this@SymCoroutine.isMain) Unit else throw IllegalStateException("SymCoroutine cannot be dead.")
        }() as T)
    }

    override fun resumeWith(result: Result<T>) {
        throw IllegalStateException("SymCoroutine cannot be dead!")
    }

    suspend fun start(value: T) {
        coroutine.resume(value)
    }
}

suspend fun main() {
    lateinit var parameter: String
    lateinit var coroutine1: SymCoroutine<String>
    lateinit var coroutine2: SymCoroutine<String>
    lateinit var coroutine3: SymCoroutine<String>
    coroutine1 = SymCoroutine.create {
        parameter = transfer(coroutine3, "d")
        parameter = transfer(SymCoroutine.main, Unit)
    }
    coroutine2 = SymCoroutine.create {
        parameter = transfer(coroutine1, "c")
    }
    coroutine3 = SymCoroutine.create {
        parameter = transfer(coroutine2, "b")
        parameter = transfer(coroutine1, "f")
    }
    SymCoroutine.main {
        transfer(coroutine3, "a")
    }
}
