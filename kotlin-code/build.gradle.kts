plugins {
    id("org.jetbrains.kotlin.jvm")
    id("io.freefair.aspectj.post-compile-weaving")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

dependencies {

    // apache commons
    api("org.apache.commons:commons-lang3:3.9")
    api("commons-io:commons-io:2.7")

    // commons codec
    api("commons-codec:commons-codec:1.17.0")

    // bouncy castle provider
    api("org.bouncycastle:bcprov-jdk18on:1.71")

    // xml
    api("org.dom4j:dom4j:2.1.3")

    // gson
    api("com.google.code.gson:gson:2.8.9")

    // okhttp
    api("com.squareup.okhttp3:okhttp:+")
    api("com.squareup.okio:okio:+")

    // retrofit
    api("com.squareup.retrofit2:retrofit:+")
    api("com.squareup.retrofit2:adapter-rxjava3:+")
    api("com.squareup.retrofit2:converter-gson:+")

    // rxjava
    api("io.reactivex.rxjava3:rxjava:+")
    api("io.reactivex:rxandroid:+")

    // websocket
    api("org.java-websocket:Java-WebSocket:1.5.1")

    // AspectJ
    api("org.aspectj:aspectjrt:1.9.6")
    api("org.aspectj:aspectjweaver:1.9.6")
}