package x.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.delay

suspend fun main() {
   val broadcast = GlobalScope.broadcast<Int>(capacity = 5) {
       List(3){

       }
    }
    println("Done!")
    delay(9999)
}