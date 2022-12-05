plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "me.khol.compose.playground"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
}

dependencies {
    implementation(project(":ui:common"))
    implementation("androidx.activity:activity-compose:1.6.1")
}
