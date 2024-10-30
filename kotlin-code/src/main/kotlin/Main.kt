package x.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import x.kotlin.commons.coroutine.XDispatchers
import x.kotlin.commons.debug.XConsole.printWithThreadInfo

suspend fun main() {
    val dispatcher = XDispatchers.share()
    GlobalScope.launch(dispatcher) {
        for (i in 1..5) {
            delay(100)
            printWithThreadInfo("1")
        }
    }
    delay(10 * 1000L)
}