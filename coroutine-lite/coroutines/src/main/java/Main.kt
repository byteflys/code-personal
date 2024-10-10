import com.bennyhuo.kotlin.coroutines.cancel.suspendCancellableCoroutine
import com.bennyhuo.kotlin.coroutines.core.AbstractCoroutine
import com.bennyhuo.kotlin.coroutines.delay
import com.bennyhuo.kotlin.coroutines.launch
import com.bennyhuo.kotlin.coroutines.runBlocking
import kotlin.coroutines.resume

suspend fun main() {
    runBlocking {
        val parent = launch {
            println(1)
            delay(1000)
        } as AbstractCoroutine<Unit>
        println(2)
        // when suspend
        suspendCancellableCoroutine<Unit> { continuation ->
            println(2.1)
            val disposable = parent.doOnCompleted { result ->
                println(3)
                continuation.resume(Unit)
            }
            continuation.invokeOnCancellation { disposable.dispose() }
            println(2.2)
        }
        println(4)
    }
}