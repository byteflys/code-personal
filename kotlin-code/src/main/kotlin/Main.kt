import kotlin.reflect.KProperty

fun main() {
    val hello = Hello()
    hello.name = "tom"
    println(hello.name)
}

class Hello {

    var name: String by Delegate()
}

class Delegate {

    private var value = ""

    operator fun getValue(thisRef: Any, property: KProperty<*>): String {
        return "${thisRef.javaClass.simpleName}::${property.name}=${value}"
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        this.value = value
    }
}