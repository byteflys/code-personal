import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
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
    js {
        browser()
        binaries.executable()
    }
}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions.target = "es2015"
}

//rootProject.plugins.withType<NodeJsRootPlugin> {
//    rootProject.the<NodeJsRootExtension>().versions.webpack.version = "5.93.0"
//    rootProject.the<NodeJsRootExtension>().versions.webpackCli.version = "5.1.4"
//}