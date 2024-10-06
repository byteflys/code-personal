package x.coroutine

import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*

internal class CoroutineImpl<P, R>(
    override var context: CoroutineContext,
    block: suspend WriteableCoroutine<P, R>.() -> R
) : Continuation<R>, WriteableCoroutine<P, R>, ReadableCoroutine<P, R> {

    override var parameter: P? = null

    private val status: AtomicReference<Status>

    init {
        val continuation = block.createCoroutine(this, this)
        status = AtomicReference(Status.Created(continuation))
    }

    override fun completed() = status.get() is Status.Completed

    override suspend fun yield(result: R): P = suspendCoroutine { continuation ->
        val previous = status.getAndUpdate {
            when (it) {
                is Status.Resumed<*> -> Status.Suspended(continuation)
                else -> throw IllegalStateException()
            }
        }
        (previous as Status.Resumed<R>).continuation.resume(result)
    }

    override suspend fun resume(param: P): R = suspendCoroutine { continuation ->
        val previous = status.getAndUpdate {
            when (it) {
                is Status.Created,
                is Status.Suspended<*> -> {
                    parameter = param
                    Status.Resumed(continuation)
                }
                else -> throw IllegalStateException()
            }
        }
        when (previous) {
            is Status.Created -> previous.continuation.resume(Unit)
            is Status.Suspended<*> -> (previous as Status.Suspended<P>).continuation.resume(param)
            else -> {}
        }
    }

    override fun resumeWith(result: Result<R>) {
        val previous = status.getAndUpdate {
            when (it) {
                is Status.Resumed<*> -> Status.Completed
                else -> throw IllegalStateException()
            }
        }
        (previous as Status.Resumed<R>).continuation.resumeWith(result)
    }
}