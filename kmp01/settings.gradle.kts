enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://jitpack.io/")
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://jitpack.io/")
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

buildscript {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://jitpack.io/")
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.21" apply false
    id("org.jetbrains.kotlin.multiplatform") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("org.jetbrains.compose") version "1.7.0" apply false
    id("com.android.library") version "8.5.2" apply false
    id("com.android.application") version "8.5.2" apply false
}

include(":server")
include(":shared")
include(":composeApp")