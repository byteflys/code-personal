package com.code.kotlin

fun main() {

    val producer = create<String, Unit> {
        yield("a")
        println("produce a")
        yield("b")
        println("produce b")
        yield("c")
        println("produce c")
    }

    val consumer = create<Unit, String> {
        val result1 = yield(Unit)
        println("consume $result1")
        val result2 = yield(Unit)
        println("consume $result2")
        val result3 = yield(Unit)
        println("consume $result3")
    }

    while (!producer.completed() && !consumer.completed()) {
        val param = producer.resume(Unit)
        consumer.resume(param)
    }
}