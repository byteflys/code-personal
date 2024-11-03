import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
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
    }
}