package x.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay

suspend fun main() {
    val sendChannel = GlobalScope.actor<Int> {
        while (!isClosedForReceive) {
            val element = receive()
            println(element)
        }
    }
    repeat(5) {
        sendChannel.send(it + 1)
    }
    sendChannel.close()
    println(sendChannel.isClosedForSend)
    println("Done!")
    delay(9999)
}