pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.9.23" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}