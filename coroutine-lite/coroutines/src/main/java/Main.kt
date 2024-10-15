import com.bennyhuo.kotlin.coroutines.cancel.suspendCancellableCoroutine
import com.bennyhuo.kotlin.coroutines.launch
import com.bennyhuo.kotlin.coroutines.runBlocking
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume

suspend fun main() {
    runBlocking {
        launch {
            val result = suspendCancellableCoroutine<Int> { continuation ->
                val future = CompletableFuture.supplyAsync {
                    Thread.sleep(3000L)
                    return@supplyAsync 100
                }
                future.thenApply {
                    continuation.resume(it)
                }
                continuation.invokeOnCancellation {
                    println("3")
                    future.cancel(true)
                }
                println("1")
                Thread.sleep(1000L)
                continuation.cancel()
                println("2")
            }
            println(result)
        }
    }
}