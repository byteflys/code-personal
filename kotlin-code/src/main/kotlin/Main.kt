package x.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select

suspend fun main() {
    val deferred1 = GlobalScope.async {
        delay(2000)
        return@async 1
    }
    val deferred2 = GlobalScope.async {
        delay(1000)
        return@async 2
    }
    val deferred = select<Int> {
        deferred1.onAwait { it }
        deferred2.onAwait { it }
    }
    println(deferred)
    println("Done!")
    delay(9999)
}