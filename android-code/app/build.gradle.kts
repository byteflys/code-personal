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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {

    // AndroidX
    api("androidx.appcompat:appcompat:+")
    api("androidx.activity:activity-compose:+")
    api("androidx.constraintlayout:constraintlayout:+")
    api("com.google.android.material:material:+")

    // Compose
    api("androidx.compose.ui:ui:+")
    api("androidx.compose.ui:ui-graphics:+")
    api("androidx.compose.ui:ui-tooling-preview:+")
    api("androidx.compose.material3:material3:+")
    api(platform("androidx.compose:compose-bom:+"))

    // Kotlin
    implementation("androidx.core:core-ktx:1.13.1")

    // Coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:+")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:+")

    // Lifecycle
    api("androidx.lifecycle:lifecycle-runtime-ktx:+")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:+")

    // GSON
    api("com.google.code.gson:gson:+")

    // OkHttp3
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