package x.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    val parentJob = Job()
    val scope = CoroutineScope(parentJob)
    val dispatcher = Dispatchers.Default
    val start = CoroutineStart.LAZY
    val job = scope.launch(dispatcher, start) {
        println("coroutine by launch")
    }
    job.start()
    delay(999 * 1000L)
}