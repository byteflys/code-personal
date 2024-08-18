pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.9.23"
        id("io.freefair.aspectj.post-compile-weaving") version "8.7.1"
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}