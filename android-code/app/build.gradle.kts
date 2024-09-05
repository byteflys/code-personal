import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.dsl.SigningConfig

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("io.github.FlyJingFish.AndroidAop.android-aop")
}

android {
    compileSdk = 34
    defaultConfig {
        namespace = "com.android.code"
        applicationId = "com.android.code.app"
        testApplicationId = "com.android.code.test"
        minSdk = 28
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
        compose = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    flavorDimensions += listOf("channel", "version")

    productFlavors {
        create("HUAWEI") {
            dimension = "channel"
            applicationIdSuffix = ".huawei"
        }
        create("XIAOMI") {
            dimension = "channel"
            applicationIdSuffix = ".xiaomi"
        }
        create("101") {
            dimension = "version"
            applicationIdSuffix = ".v101"
        }
        create("102") {
            dimension = "version"
            applicationIdSuffix = ".v102"
        }
    }

    signingConfigs {
        create("common") {
            storeFile = File(rootProject.projectDir, "sign.keystore")
            storePassword = "123456"
            keyAlias = "signKey"
            keyPassword = "123456"
            enableV2Signing = true
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("common")
        }
        debug {
            signingConfig = signingConfigs.getByName("common")
        }
        names.forEach { name ->
            named(name) {
                configureBuildType(signingConfigs, applicationVariants)
            }
        }
    }
}

fun ApplicationBuildType.configureBuildType(
    signingConfigs: NamedDomainObjectContainer<SigningConfig>,
    applicationVariants: DomainObjectSet<ApplicationVariant>
) {
    signingConfig = signingConfigs.getByName("common")
    applicationVariants.all { variant ->
        if (name == "debug") {
            return@all false
        }
        val name = "${variant.flavorName}-${variant.versionName}.apk"
        println("Configure Variant -> Package Name : " + name)
        variant.productFlavors.forEach { flavor ->
            println("Configure Variant -> Flavor Name : " + flavor.name)
        }
        variant.outputs.forEach { output ->
            println("Configure Variant -> Output Name : " + output.name)
        }
        return@all true
    }
}

dependencies {

    // AndroidX
    api("androidx.appcompat:appcompat:1.7.0")
    api("androidx.fragment:fragment-ktx:1.8.2")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("com.google.android.material:material:1.12.0")

    // Compose
//    api("androidx.compose.ui:ui:+")
//    api("androidx.compose.ui:ui-graphics:+")
//    api("androidx.compose.ui:ui-tooling-preview:+")
//    api("androidx.compose.material3:material3:+")
//    api("androidx.activity:activity-compose:+")
//    api(platform("androidx.compose:compose-bom:+"))

    // Kotlin
    api("androidx.core:core-ktx:1.13.1")

    // Coroutine
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Lifecycle
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")

    // GSON
    api("com.google.code.gson:gson:2.11.0")

    // OkHttp3
//    api("com.squareup.okhttp3:okhttp:+")
//    api("com.squareup.okhttp3:logging-interceptor:+")

    // RxJava3
    api("io.reactivex.rxjava3:rxjava:3.1.9")
    api("io.reactivex.rxjava3:rxandroid:3.0.2")
    api("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    // Retrofit2
//    api("com.squareup.retrofit2:retrofit:+")
//    api("com.squareup.retrofit2:converter-gson:+")
//    api("com.squareup.retrofit2:adapter-rxjava2:+")

    // MMKV
//    api("com.tencent:mmkv-static:+")
}