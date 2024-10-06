package x.coroutine

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor

// theory of Dispatcher & Interceptor
object Dispatchers {

    private val currentExecutor = Executor { runnable -> runnable.run() }

    private val newExecutor = Executors.newCachedThreadPool()

    private val dataExecutor = Executors.newSingleThreadExecutor()

    private val networkExecutor = Executors.newScheduledThreadPool(10)

    fun current() = Dispatcher(currentExecutor)

    fun new() = Dispatcher(newExecutor)

    fun data() = Dispatcher(dataExecutor)

    fun network() = Dispatcher(networkExecutor)

    fun sequence() = Dispatcher(Executors.newSingleThreadExecutor())
}

// delegate continuation work to another continuation
// which supports thread schedule
class Dispatcher(
    val executor: Executor? = null
) : ContinuationInterceptor {

    override val key = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return DispatcherContinuation(continuation, executor)
    }
}

// continuation that supports thread schedule
class DispatcherContinuation<T>(
    val continuation: Continuation<T>,
    val executor: Executor? = null
) : Continuation<T> by continuation {

    override fun resumeWith(result: Result<T>) {
        if (executor == null) {
            return continuation.resumeWith(result)
        }
        executor.execute {
            continuation.resumeWith(result)
        }
    }
}