import com.bennyhuo.kotlin.coroutines.launch
import com.bennyhuo.kotlin.coroutines.runBlocking
import com.bennyhuo.kotlin.coroutines.scope.CoroutineScope

fun main() {
    runBlocking {
        try {
            runCoroutine()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
    Thread.sleep(999 * 1000L)
}

suspend fun CoroutineScope.runCoroutine() {
    val job = launch {
        println(1)
        throw RuntimeException("ea")
        println(2)
    }
    println(3)
    job.join()
    println(4)
}