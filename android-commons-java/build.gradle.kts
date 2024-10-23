plugins {
    kotlin("jvm") version "1.9.23"
}

dependencies {
    api("com.google.code.gson:gson:+")
    api("io.reactivex.rxjava3:rxjava:+")
    api("io.reactivex:rxandroid:+")
    api("com.squareup.okhttp3:okhttp:+")
    api("com.squareup.okio:okio:+")
    api("com.squareup.retrofit2:retrofit:+")
    api("com.squareup.retrofit2:adapter-rxjava3:+")
    api("com.squareup.retrofit2:converter-gson:+")
    api("org.java-websocket:Java-WebSocket:1.5.1")
}