import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun main() {
    Observable.just(0)
        .map {
            Thread.sleep(2000L)
        }
        .timeout(1000L, TimeUnit.MILLISECONDS)
        .doOnNext {
            println("No Timeout")
        }
        .doOnError {
            if (it is TimeoutException) {
                println("No Timeout")
            }
        }
        .blockingSubscribe()
}