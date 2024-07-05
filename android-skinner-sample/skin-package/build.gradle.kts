plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    defaultConfig {
        namespace = "com.android.app"
        applicationId = "com.android.app"
        minSdk = 30
    }

    sourceSets {
        getByName("main").res.srcDirs("src/main/res-dark")
    }
}
