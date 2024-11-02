import org.jetbrains.kotlin.ir.backend.js.lower.collectNativeImplementations

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
}

include(":app")
