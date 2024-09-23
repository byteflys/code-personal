package com.code.kotlin

fun main() {
    async {
        val userName = await<String> { loadUserName() }
        println(userName)
    }
}

fun Promise<String>.loadUserName() {
    try {
        resume("hello")
    } catch (e: Throwable) {
        error(e)
    }
}
