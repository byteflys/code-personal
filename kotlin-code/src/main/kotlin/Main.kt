import io.reactivex.rxjava3.core.Observable

var x = -1

fun main() {
    val list = mutableListOf<Int>()
    for (i in 0 until 1000000){
        list.add(i)
    }
    Observable.fromIterable(list)
        .map {
            x = it
        }
        .flatMap {
            printXA()
        }
        .flatMap {
            printXB()
        }
        .blockingSubscribe()
}

fun printXA():Observable<Int>{
    return Observable.just(x)
        .doOnNext {
            println("A$it")
        }
}

fun printXB():Observable<Int>{
    return Observable.just(x)
        .doOnNext {
            println("A$it")
        }
}

