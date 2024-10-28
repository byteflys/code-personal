package x.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        printWithThreadInfo(throwable.message + " " + "DefaultUncaughtExceptionHandler")
    }
    val errorHandler = CoroutineExceptionHandler { context, throwable ->
        println(throwable.message + " " + context[CoroutineName])
    }
    val job1 = GlobalScope.launch(Dispatchers.Unconfined + errorHandler + CoroutineName("1")) {
        val job2 = launch(Dispatchers.Unconfined + errorHandler + CoroutineName("2")) {
            delay(100)
            throw RuntimeException("crash")
        }
        println("2")
    }
    println("1")
    delay(999 * 1000L)
}