package x.kotlinmultiplatform.helloworld

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform