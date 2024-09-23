package com.code.kotlin

import kotlin.concurrent.thread

fun main() {
    async {
        val user = await<String> { loadUserInfo() }
        println(user)
        println("async returned")
    }
    println("async not return")
}

fun Promise<String>.loadUserInfo() {
    thread {
        Thread.sleep(3000L)
        try {
            resume("Darius")
        } catch (e: Throwable) {
            error(e)
        }
    }
}
