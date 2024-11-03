package x.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform