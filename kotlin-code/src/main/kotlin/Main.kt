package com.code.kotlin

fun main() {

    val producer = create<String, Unit> {
        yield("a")
        yield("b")
        yield("c")
    }

    val consumer = create<Unit, String> {
        val result1 = yield(Unit)
        println("consume $result1")
        val result2 = yield(Unit)
        println("consume $result2")
        val result3 = yield(Unit)
        println("consume $result3")
    }

    val param1 = producer.resume(Unit)
    println("produce $param1")
    consumer.resume(param1)
    val param2 = producer.resume(Unit)
    println("produce $param2")
    consumer.resume(param2)
    val param3 = producer.resume(Unit)
    println("produce $param3")
    consumer.resume(param3)
}