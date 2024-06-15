import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

fun main() {

}

fun createSingleThreadScheduler(name: String): Scheduler {
    val threadFactory = ThreadFactory { runnable ->
        val thread = Thread(runnable)
        thread.name = name
        return@ThreadFactory thread
    }
    val executor = Executors.newSingleThreadScheduledExecutor(threadFactory)
    return Schedulers.from(executor)
}