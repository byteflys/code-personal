plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34
    defaultConfig {
        namespace = "com.android.code"
        applicationId = "com.android.code.app"
        testApplicationId = "com.android.code.test"
        minSdk = 30
        targetSdk = 34
    }
}

dependencies {

    // AndroidX
    api("androidx.appcompat:appcompat:+")
    api("androidx.constraintlayout:constraintlayout:+")
    api("com.google.android.material:material:+")

    // Coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:+")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:+")

    // Lifecycle
    api("androidx.lifecycle:lifecycle-runtime-ktx:+")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:+")

    // GSON
    api("com.google.code.gson:gson:+")

    // OkHttp
    api("com.squareup.okhttp3:okhttp:+")
    api("com.squareup.okhttp3:logging-interceptor:+")

    // RxJava3
    api("io.reactivex.rxjava3:rxjava:+")
    api("io.reactivex.rxjava3:rxandroid:+")
    api("com.github.akarnokd:rxjava3-retrofit-adapter:+")

    // Retrofit2
    api("com.squareup.retrofit2:retrofit:+")
    api("com.squareup.retrofit2:converter-gson:+")
    api("com.squareup.retrofit2:adapter-rxjava2:+")

    // MMKV
    api("com.tencent:mmkv-static:+")
}