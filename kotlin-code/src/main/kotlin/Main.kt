package com.code.kotlin

fun main() {

    val producer = create<Unit, String> {
        yield("a")
        println("produce a")
        yield("b")
        println("produce b")
        yield("c")
        println("produce c")
    }

    val consumer = create<String, Unit> {
        val param1 = yield(Unit)
        println("consume $param1")
        val param2 = yield(Unit)
        println("consume $param2")
        val param3 = yield(Unit)
        println("consume $param3")
    }

    val out1 = producer.resume(Unit)
    consumer.resume(out1)
    val out2 = producer.resume(Unit)
    consumer.resume(out2)
    val out3 = producer.resume(Unit)
    consumer.resume(out3)
}