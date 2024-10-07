package x.coroutine

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface SymCoroutineScope<T> {
    suspend fun <P> transfer(symCoroutine: SymCoroutine<P>, value: P): T
}

class SymCoroutine<T>(
    val context: CoroutineContext = EmptyCoroutineContext,
    private val block: suspend SymCoroutineScope<T>.() -> Unit
) {

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
        private tailrec suspend fun <P> transferInner(other: SymCoroutine<P>, value: Any?): T {
            if (!this@SymCoroutine.isMain)
                return coroutine.yield(Parameter(other, value as P))
            return if (other.isMain) {
                value as T
            } else {
                val parameter = other.coroutine.resume(value as P)
                transferInner(parameter.coroutine, parameter.value)
            }
        }

        override suspend fun <P> transfer(symCoroutine: SymCoroutine<P>, value: P): T {
            return transferInner(symCoroutine, value)
        }
    }

    private val coroutine = CoroutineImpl<T, Parameter<*>>(context) {
        Parameter(this@SymCoroutine, suspend {
            block(body)
        }() as T)
    }

    suspend fun start(value: T) {
        coroutine.resume(value)
    }
}

suspend fun main() {
    lateinit var coroutine1: SymCoroutine<String>
    lateinit var coroutine2: SymCoroutine<String>
    lateinit var coroutine3: SymCoroutine<String>
    coroutine1 = SymCoroutine.create {
        transfer(coroutine3, "d")
        transfer(SymCoroutine.main, Unit)
        println("coroutine1 finished")
    }
    coroutine2 = SymCoroutine.create {
        transfer(coroutine1, "c")
        println("coroutine2 finished")
    }
    coroutine3 = SymCoroutine.create {
        transfer(coroutine2, "b")
        transfer(coroutine1, "e")
        println("coroutine3 finished")
    }
    SymCoroutine.main {
        transfer(coroutine3, "a")
        println("coroutine main finished")
    }
    println("main finished")
}
