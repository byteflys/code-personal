pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/central/")
        maven("https://maven.aliyun.com/repository/gradle-plugin/")
        maven("https://maven.aliyun.com/repository/apache-snapshots/")
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io/")
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/central/")
        maven("https://maven.aliyun.com/repository/gradle-plugin/")
        maven("https://maven.aliyun.com/repository/apache-snapshots/")
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io/")
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

buildscript {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/central/")
        maven("https://maven.aliyun.com/repository/gradle-plugin/")
        maven("https://maven.aliyun.com/repository/apache-snapshots/")
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io/")
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.code.gson:gson:+")
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.android.library") version "8.1.2" apply false
    id("com.google.devtools.ksp") version "2.0.10-1.0.24" apply false
    id("org.jetbrains.kotlin.android") version "2.0.10" apply false
    id("io.github.FlyJingFish.AndroidAop.android-aop") version "2.0.9" apply false
}

include(":app")