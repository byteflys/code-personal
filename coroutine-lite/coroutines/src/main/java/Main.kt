import com.bennyhuo.kotlin.coroutines.delay
import com.bennyhuo.kotlin.coroutines.dispatcher.Dispatchers
import com.bennyhuo.kotlin.coroutines.launch
import com.bennyhuo.kotlin.coroutines.runBlocking

suspend fun main() {
    runBlocking {
        val job = launch {
            println(1)
            delay(1000)
            println(2)
        }
        println(3)
        job.join()
        println(4)
    }
}