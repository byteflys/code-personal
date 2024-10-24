pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.0.0" apply false
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