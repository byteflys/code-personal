package x.coroutine

import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*

sealed class Status {
    internal class Created(val continuation: Continuation<Unit>) : Status()
    internal class Suspended<P>(val continuation: Continuation<P>) : Status()
    internal class Resumed<R>(val continuation: Continuation<R>) : Status()
    internal data object Completed : Status()
}

interface CoroutineScope<P, R> {

    val parameter: P?

    suspend fun yield(value: R): P
}

class Coroutine<P, R>(
    override val context: CoroutineContext = EmptyCoroutineContext,
    private val block: suspend CoroutineScope<P, R>.() -> R
) : Continuation<R> {

    companion object {
        fun <P, R> create(
            context: CoroutineContext = EmptyCoroutineContext,
            block: suspend CoroutineScope<P, R>.() -> R
        ): Coroutine<P, R> {
            return Coroutine(context, block)
        }
    }

    private val scope = object : CoroutineScope<P, R> {
        override var parameter: P? = null

        override suspend fun yield(value: R): P = suspendCoroutine { continuation ->
            val previousStatus = status.getAndUpdate {
                when (it) {
                    is Status.Created -> throw IllegalStateException("Never started!")
                    is Status.Suspended<*> -> throw IllegalStateException("Already yielded!")
                    is Status.Resumed<*> -> Status.Suspended(continuation)
                    is Status.Completed -> throw IllegalStateException("Already dead!")
                }
            }

            (previousStatus as? Status.Resumed<R>)?.continuation?.resume(value)
        }
    }

    private val status: AtomicReference<Status>

    fun active(): Boolean {
        return status.get() !is Status.Completed
    }

    init {
        val coroutineBlock: suspend CoroutineScope<P, R>.() -> R = { block() }
        val start = coroutineBlock.createCoroutine(scope, this)
        status = AtomicReference(Status.Created(start))
    }

    override fun resumeWith(result: Result<R>) {
        val previousStatus = status.getAndUpdate {
            when (it) {
                is Status.Created -> throw IllegalStateException("Never started!")
                is Status.Suspended<*> -> throw IllegalStateException("Already yielded!")
                is Status.Resumed<*> -> {
                    Status.Completed
                }
                is Status.Completed -> throw IllegalStateException("Already dead!")
            }
        }
        (previousStatus as? Status.Resumed<R>)?.continuation?.resumeWith(result)
    }

    suspend fun resume(value: P): R = suspendCoroutine { continuation ->
        val previousStatus = status.getAndUpdate {
            when (it) {
                is Status.Created -> {
                    scope.parameter = value
                    Status.Resumed(continuation)
                }
                is Status.Suspended<*> -> {
                    scope.parameter = value
                    Status.Resumed(continuation)
                }
                is Status.Resumed<*> -> throw IllegalStateException("Already resumed!")
                is Status.Completed -> throw IllegalStateException("Already dead!")
            }
        }

        when (previousStatus) {
            is Status.Created -> previousStatus.continuation.resume(Unit)
            is Status.Suspended<*> -> (previousStatus as Status.Suspended<P>).continuation.resume(value)
            else -> {}
        }
    }

    suspend fun <SymT> SymCoroutine<SymT>.yield(value: R): P {
        return scope.yield(value)
    }
}
