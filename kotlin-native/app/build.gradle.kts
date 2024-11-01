import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    linuxX64("native") {
        binaries {
            executable()
        }
    }
    js {
        browser {
        }
        binaries.executable()
    }
}

tasks.withType<KotlinJsCompile>().configureEach {
    kotlinOptions {
        target = "es2015"
    }
}