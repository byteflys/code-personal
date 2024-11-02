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
        browser {
            commonWebpackConfig {
                outputFileName = "app.js"
                devServer?.static?.add(project.rootDir.path)
            }
        }
        binaries.executable()
        compilerOptions.target = "es5"
    }
}