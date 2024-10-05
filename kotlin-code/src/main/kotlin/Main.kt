package x.coroutine

suspend fun main() {

    printThreadId("main.start")

    val producer = Coroutine.create(Dispatchers.newThread()) { initParam: Unit ->
        for (i in 0..3) {
            printThreadId("producer.yield")
            val param = yield(i)
            printThreadId("produce $param->$i")
        }
        return@create 0
    }

    val consumer = Coroutine.create(Dispatchers.newThread()) { initParam: Int ->
        for (i in 0..3) {
            printThreadId("consumer.yield")
            val param = yield(Unit)
            printThreadId("consume $param->${Unit}")
        }
    }

    while (producer.active() && consumer.active()) {
        printThreadId("producer.resume")
        val param1 = producer.resume(Unit)
        printThreadId("consumer.resume")
        val param2 = consumer.resume(param1)
    }

    printThreadId("main.end")
}

fun printThreadId(tag: String) {
    val tid = Thread.currentThread().id
    println("$tag $tid")
}