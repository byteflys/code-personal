package com.code.kotlin

import kotlin.concurrent.thread

fun main() {
    async {
        val userName = await<String> { loadUserName() }
        println(userName)
        println("async returned")
    }
    println("async not return")
}

fun Promise<String>.loadUserName() {
    thread {
        Thread.sleep(3000L)
        try {
            resume("hello")
        } catch (e: Throwable) {
            error(e)
        }
    }
}
