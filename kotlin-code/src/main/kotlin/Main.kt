package com.code.kotlin

import io.reactivex.rxjava3.core.Observable

fun main() {
    Observable.just(Unit)
        .doOnNext { println("next 1") }
        .doOnError { println("error 1") }
        .map<String> { throw RuntimeException("error") }
        .doOnNext { println("next 2") }
        .doOnError { println("error 2") }
        .onErrorReturn { "error placeholder 3" }
        .doOnNext { println("next 3") }
        .doOnError { println("error 3") }
        .doOnComplete { println("complete 4") }
        .doFinally { println("finally 4") }
        .subscribe()
}