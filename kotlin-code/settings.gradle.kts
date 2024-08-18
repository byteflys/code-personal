pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.9.23" apply false
        id("io.freefair.aspectj.post-compile-weaving") version "8.7.1" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}