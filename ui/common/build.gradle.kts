import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

group = "me.khol.compose.playground"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose("org.jetbrains.compose.material:material-icons-extended-desktop"))
                api(compose.animationGraphics)
                api(compose.uiTooling)
                api(compose.preview)
                implementation(project(":core"))
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("androidMain") {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
            }
        }
        named("androidTest") {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        named("desktopMain") {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api(compose.preview)
            }
        }
        named("desktopTest")
    }
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}