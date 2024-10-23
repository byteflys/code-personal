package x.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

suspend fun main() {
    flow<Int> {
        repeat(5) {
            delay(300)
            emit(it + 1)
        }
    }.onEach {
        println(it)
    }.collect()
    println("Done!")
    delay(9999)
}