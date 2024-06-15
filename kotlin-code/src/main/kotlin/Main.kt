import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun main() {
    Observable.just(0)
        .map {
            Thread.sleep(2000L)
        }
        .timeout(1000L,TimeUnit.MILLISECONDS)
        .doOnNext {
            println("No Timeout")
        }
        .doOnError { throw it }
        .subscribe()
    Thread.sleep(5000L)
}