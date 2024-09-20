plugins {
    id("org.jetbrains.kotlin.jvm")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

dependencies {

    // kotlin & coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // apache commons
    api("commons-io:commons-io:2.7")
    api("org.apache.commons:commons-lang3:3.9")

    // gson
    api("com.google.code.gson:gson:2.8.9")

    // rxjava
    api("io.reactivex.rxjava3:rxjava:+")

    // okhttp
    api("com.squareup.okhttp3:okhttp:+")
    api("com.squareup.okio:okio:+")

    // retrofit
    api("com.squareup.retrofit2:retrofit:+")
    api("com.squareup.retrofit2:adapter-rxjava3:+")
    api("com.squareup.retrofit2:converter-gson:+")

    // websocket
    api("org.java-websocket:Java-WebSocket:1.5.1")
}