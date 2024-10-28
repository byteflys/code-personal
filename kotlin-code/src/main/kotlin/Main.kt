package x.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import x.kotlin.commons.coroutine.CoroutineDispatchers
import x.kotlin.commons.debug.Debug.printWithThreadInfo

suspend fun main() {
    val dispatcher1 = CoroutineDispatchers.io()
    val dispatcher2 = CoroutineDispatchers.data()
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        printWithThreadInfo(throwable.message + " " + "DefaultUncaughtExceptionHandler")
    }
    val errorHandler = CoroutineExceptionHandler { context, throwable ->
        printWithThreadInfo(throwable.message + " " + context[CoroutineName])
    }
    val job1 = GlobalScope.launch(dispatcher1 + errorHandler + CoroutineName("1")) {
        val job2 = GlobalScope.launch(dispatcher2 + errorHandler + CoroutineName("2")) {
            delay(100)
            printWithThreadInfo("1")
            throw RuntimeException("crash")
        }
        delay(200)
        printWithThreadInfo("2")
    }
    delay(300)
    printWithThreadInfo("3")
    delay(999 * 1000L)
}