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

    val group = "androidx.compose"
    implementation("$group.animation:animation")
    implementation("$group.animation:animation-graphics")
    implementation("$group.foundation:foundation")
    implementation("$group.material:material")
//    implementation("$group.material3:material3")
    implementation("$group.runtime:runtime")
    implementation("$group.ui:ui")
//    implementation("$group.ui:ui-test-junit4")
    implementation("$group.ui:ui-tooling")
    implementation("$group.ui:ui-tooling-preview")
//    implementation("$group.material:material-icons-extended")
}
