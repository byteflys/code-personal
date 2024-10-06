package x.coroutine

suspend fun main() {

    printThreadId("main.start")

    val producer = Coroutines.launch<Unit, Int>(Dispatchers.new()) {
        for (i in 1..3) {
            printThreadId("producer.yieldWith $i")
            yield(i)
        }
        return@launch 0
    }

    val consumer = Coroutines.launch<Int, Unit>(Dispatchers.new()) {
        for (i in 1..3) {
            println("$parameter consumed")
            printThreadId("consumer.yieldWith Unit")
            yield(Unit)
        }
    }

    while (producer.active() && consumer.active()) {
        printThreadId("producer.resumeWith Unit")
        val param1 = producer.resume(Unit)
        printThreadId("consumer.resumeWith $param1")
        val param2 = consumer.resume(param1)
    }

    printThreadId("main.end")
}

fun printThreadId(tag: String) {
    val tid = Thread.currentThread().id
    println("$tag <tid=$tid>")
}