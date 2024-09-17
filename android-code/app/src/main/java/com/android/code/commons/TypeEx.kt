typealias NoArgCallbackFunc = () -> Unit
typealias SingleArgCallbackFunc<T> = (T) -> Unit
typealias DoubleArgCallbackFunc<T, R> = (T, R) -> Unit
typealias ThreeArgCallbackFunc<T, R, S> = (T, R, S) -> Unit

val EmptyBiFunction = { o1: Any, o2: Any -> Unit }

class IntList : ArrayList<Int>()

class LongList : ArrayList<Long>()

class FloatList : ArrayList<Float>()

class DoubleList : ArrayList<Double>()

class BooleanList : ArrayList<Boolean>()

class StringList : ArrayList<String>()

class StringMap : LinkedHashMap<String, String>()

fun List<Int>?.toIntList(): IntList {
    val src = this
    return IntList().apply {
        if (src != null)
            addAll(src)
    }
}

fun List<Long>?.toLongList(): LongList {
    val src = this
    return LongList().apply {
        if (src != null)
            addAll(src)
    }
}

fun List<Float>?.toFloatList(): FloatList {
    val src = this
    return FloatList().apply {
        if (src != null)
            addAll(src)
    }
}

fun List<Double>?.toDoubleList(): DoubleList {
    val src = this
    return DoubleList().apply {
        if (src != null)
            addAll(src)
    }
}

fun List<Boolean>?.toBooleanList(): BooleanList {
    val src = this
    return BooleanList().apply {
        if (src != null)
            addAll(src)
    }
}

fun List<String>?.toStringList(): StringList {
    val src = this
    return StringList().apply {
        if (src != null)
            addAll(src)
    }
}

fun IntArray?.toIntList(): IntList {
    val src = this
    return IntList().apply {
        if (src != null)
            addAll(src.toList())
    }
}