package com.code.kotlin

fun main() {
    val generator = generator {
        yield(100)
        yield(200)
        yield(300)
    }
    for (value in generator) {
        println(value)
    }
}
