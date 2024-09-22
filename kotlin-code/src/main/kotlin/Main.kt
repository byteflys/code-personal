package com.code.kotlin

fun main() {
    val generator = generator {
        yield(100)
        yield(200)
        yield(300)
    }
    while (generator.hasNext()) {
        val value = generator.await()
        println(value)
    }
}
