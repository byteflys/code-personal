package x.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import x.kotlin.commons.coroutine.XDispatchers
import x.kotlin.commons.debug.XConsole.printWithThreadInfo

suspend fun main() {
    val dispatcher1 = XDispatchers.new()
    val dispatcher2 = XDispatchers.new()
    val errorHandler = CoroutineExceptionHandler { context, throwable ->
        printWithThreadInfo(context[CoroutineName]?.name + "-crashed")
    }
    val job1 = GlobalScope.launch(dispatcher1 + errorHandler + CoroutineName("Job1")) {
        val supervisorScope = CoroutineScope(SupervisorJob())
        supervisorScope.launch {
            val job2 = launch(dispatcher2 + errorHandler + CoroutineName("Job2")) {
                delay(500)
                printWithThreadInfo("Job2")
                throw RuntimeException("crash")
            }
        }
        delay(200)
        printWithThreadInfo("Job1")
    }
    delay(400)
    printWithThreadInfo("Main")
    delay(10 * 1000L)
}