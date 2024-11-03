import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.android.application")
}

kotlin {
    // gradle jvmRun
    jvm {
        compilerOptions.jvmTarget = JvmTarget.JVM_1_8
        mainRun {
            mainClass.set("JvmMainKt")
        }
    }
    // gradle macMainBinaries
    // app/build/bin/mac/debugExecutable/app.kexe
    macosX64("mac") {
        binaries.executable()
    }
    // create index.html in resources
    // include app.js in html
    // gradle jsRun --continuous
    // gradle jsBrowserDevelopmentRun --continuous
    // gradle jsNodeRun
    js {
        browser {
            commonWebpackConfig {
                outputFileName = "app.js"
                devServer?.static?.add(project.rootDir.path)
            }
        }
        nodejs()
        binaries.executable()
        compilerOptions.target = "es5"
    }
    androidTarget {
        compilerOptions.jvmTarget = JvmTarget.JVM_1_8
    }
    iosArm64("ios") {
        binaries.framework {
            baseName = "app"
            isStatic = true
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:2.8.3")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0")
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation("androidx.activity:activity-compose:1.9.3")
        }
        iosMain.dependencies {
            implementation("org.jetbrains.compose.ui:ui-uikitarm64:1.7.0")
        }
    }
}

android {
    namespace = "x.kmp.hello"
    compileSdk = 34
    defaultConfig {
        applicationId = "x.kmp.hello"
        minSdk = 30
        targetSdk = 34
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}