import com.bennyhuo.kotlin.coroutines.launch
import com.bennyhuo.kotlin.coroutines.runBlocking

suspend fun main() {
    runBlocking {
        val parent = launch {
            println(1)
            val child = launch {
                println(2)
            }
            child.cancel()
            child.join()
            println(3)
        }
        parent.join()
        println(4)
    }
}