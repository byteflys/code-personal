plugins {
    kotlin("jvm") version "1.9.23"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

dependencies {

    // apache commons
    api("org.apache.commons:commons-lang3:3.9")
    api("commons-io:commons-io:2.6")
    api("commons-codec:commons-codec:1.15")

    // xml
    api("org.dom4j:dom4j:2.1.1")

    // gson
    api("com.google.code.gson:gson:2.8.5")

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
}