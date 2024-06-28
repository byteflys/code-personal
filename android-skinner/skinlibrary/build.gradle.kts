plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34
    defaultConfig {
        namespace = "com.android.library.skinner"
        minSdk = 30
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {

    // Kotlin
    api("androidx.core:core-ktx:1.13.1")

    // AndroidX
    api("androidx.appcompat:appcompat:+")
    api("androidx.constraintlayout:constraintlayout:+")

    // MaterialDesign
    api("com.google.android.material:material:+")

    // MMKV
    api("com.tencent:mmkv-static:+")
}