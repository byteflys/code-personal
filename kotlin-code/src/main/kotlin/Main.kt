package x.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import x.kotlin.commons.coroutine.CoroutineDispatchers
import x.kotlin.commons.debug.Debug.printWithThreadInfo

suspend fun main() {
    val dispatcher1 = CoroutineDispatchers.new()
    val dispatcher2 = CoroutineDispatchers.data()
    val errorHandler = CoroutineExceptionHandler { context, throwable ->
        printWithThreadInfo(context[CoroutineName]?.name + "-crashed")
    }
    lateinit var job2: Job
    val job1 = GlobalScope.launch(dispatcher1 + errorHandler + CoroutineName("Job1")) {
          job2 = launch(dispatcher2 + errorHandler + CoroutineName("Job2")) {
            delay(200)
            printWithThreadInfo("Job2")
            throw RuntimeException("crash")
        }
        delay(400)
        printWithThreadInfo("Job1")
    }
    delay(600)
    job1.join()
    job2.join()
    printWithThreadInfo("Main")
    delay(1 * 1000L)
}