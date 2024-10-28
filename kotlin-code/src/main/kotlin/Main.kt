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
    val dispatcher1 = CoroutineDispatchers.io()
    val dispatcher2 = CoroutineDispatchers.data()
    val errorHandler = CoroutineExceptionHandler { context, throwable ->
        printWithThreadInfo(context[CoroutineName]?.name + "-crashed")
    }
    lateinit var job1: Job
    job1 = GlobalScope.launch(dispatcher1 + errorHandler + CoroutineName("Job1")) {
        val job2 = launch(dispatcher2 + errorHandler + CoroutineName("Job2")) {
            delay(100)
            printWithThreadInfo("1")
            println("Job1.isCompleted = ${job1.isCompleted}")
            throw RuntimeException("crash")
        }
        printWithThreadInfo("2")
    }
    delay(400)
    printWithThreadInfo("3")
    println("Job1.isCompleted = ${job1.isCompleted}")
    delay(999 * 1000L)
}