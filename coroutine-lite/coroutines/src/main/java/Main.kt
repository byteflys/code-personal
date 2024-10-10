import com.bennyhuo.kotlin.coroutines.cancel.suspendCancellableCoroutine
import com.bennyhuo.kotlin.coroutines.core.AbstractCoroutine
import com.bennyhuo.kotlin.coroutines.launch
import com.bennyhuo.kotlin.coroutines.runBlocking
import kotlin.coroutines.resume

suspend fun main() {
    runBlocking {
        val parent = launch {
            println(1)
        } as AbstractCoroutine<Unit>
        // when suspend
        suspendCancellableCoroutine<Unit> { continuation ->
            val disposable = parent.doOnCompleted { result ->
                println(2)
                continuation.resume(Unit)
            }
            continuation.invokeOnCancellation { disposable.dispose() }
        }
    }
}