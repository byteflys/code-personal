pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.0.0" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}