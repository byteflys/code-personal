package x.coroutine

suspend fun main() {
    val list = mutableListOf<SymmetricCoroutine<*>>()
    lateinit var coroutine0: SymmetricCoroutine<Unit>
    lateinit var coroutine1: SymmetricCoroutine<String>
    lateinit var coroutine2: SymmetricCoroutine<String>
    lateinit var coroutine3: SymmetricCoroutine<String>
    coroutine1 = createSymmetric {
        transfer(coroutine3, "d")
        transfer(coroutine0, Unit)
        list.remove(coroutine1)
    }
    coroutine2 = createSymmetric {
        transfer(coroutine1, "c")
        list.remove(coroutine2)
    }
    coroutine3 = createSymmetric {
        val parameter = getParameter()
        println(parameter)
        transfer(coroutine2, "b")
        transfer(coroutine1, "e")
        list.remove(coroutine3)
    }
    coroutine0 = createSymmetric {
        transfer(coroutine3, "a")
        list.remove(coroutine0)
    }
    list.add(coroutine0)
    list.add(coroutine1)
    list.add(coroutine2)
    list.add(coroutine3)
    launchSymmetric(coroutine0, Unit)
    println(list.size)
}