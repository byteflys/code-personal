import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
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
    // gradle linuxMainBinaries
    // app/build/bin/linux/debugExecutable/app.kexe
    linuxX64("linux") {
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