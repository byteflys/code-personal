package x.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

suspend fun main() {
    val receiveChannel = GlobalScope.produce<Int> {
        repeat(5) {
            delay(300)
            send(it)
        }
        close()
        repeat(5) {
            delay(300)
            send(it)
        }
    }
    for (element in receiveChannel) {
        println(element)
    }
    println("Done!")
}