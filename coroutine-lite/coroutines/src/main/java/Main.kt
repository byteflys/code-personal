import com.bennyhuo.kotlin.coroutines.async
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
    val job = async {
        println(1)
        throw RuntimeException("ea")
        println(2)
        return@async 100
    }
    println(3)
    val ret = job.await()
    println(ret)
}