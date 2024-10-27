package x.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    val job = GlobalScope.launch {
        delay(1000)
        println("coroutine by launch")
    }
    println(job.isCancelled)
    delay(999 * 1000L)
}