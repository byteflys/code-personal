package x.coroutine

import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*

interface CoroutineScope<P, R> {

    var context: CoroutineContext

    var parameter: P?

    suspend fun yield(result: R): P
}

class Coroutineee<P, R>(
    override var context: CoroutineContext = EmptyCoroutineContext,
    private val block: suspend CoroutineScope<P, R>.() -> R
) : Continuation<R>, CoroutineScope<P, R> {

    override var parameter: P? = null

    override suspend fun yield(result: R): P = suspendCoroutine { continuation ->
        val previousStatus = status.getAndUpdate {
            when (it) {
                is Status.Created -> throw IllegalStateException("Never started!")
                is Status.Suspended<*> -> throw IllegalStateException("Already yielded!")
                is Status.Resumed<*> -> Status.Suspended(continuation)
                is Status.Completed -> throw IllegalStateException("Already dead!")
            }
        }

        (previousStatus as? Status.Resumed<R>)?.continuation?.resume(result)
    }

    private val status: AtomicReference<Status>

    fun active(): Boolean {
        return status.get() !is Status.Completed
    }

    init {
        val coroutineBlock: suspend CoroutineScope<P, R>.() -> R = { block() }
        val start = coroutineBlock.createCoroutine(this, this)
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

    suspend fun resume(param: P): R = suspendCoroutine { continuation ->
        val previousStatus = status.getAndUpdate {
            when (it) {
                is Status.Created -> {
                    parameter = param
                    Status.Resumed(continuation)
                }
                is Status.Suspended<*> -> {
                    parameter = param
                    Status.Resumed(continuation)
                }
                is Status.Resumed<*> -> throw IllegalStateException("Already resumed!")
                is Status.Completed -> throw IllegalStateException("Already dead!")
            }
        }

        when (previousStatus) {
            is Status.Created -> previousStatus.continuation.resume(Unit)
            is Status.Suspended<*> -> (previousStatus as Status.Suspended<P>).continuation.resume(param)
            else -> {}
        }
    }
}
