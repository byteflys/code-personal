package x.coroutine

suspend fun main() {
    lateinit var coroutine1: SymmetricCoroutine<String>
    lateinit var coroutine2: SymmetricCoroutine<String>
    lateinit var coroutine3: SymmetricCoroutine<String>
    coroutine1 = createSymmetric {
        println("parameter ${getParameter()}")
        transfer(coroutine3, "d")
    }
    coroutine2 = createSymmetric {
        transfer(coroutine1, "c")
    }
    coroutine3 = createSymmetric {
        println("symmetric start")
        transfer(coroutine2, "b")
        transfer(coroutine1, "e")
    }
    val main = launchSymmetric(coroutine3, "a")
    coroutine1.clean()
    coroutine2.clean()
    coroutine3.clean()
    println("symmetric end")
}