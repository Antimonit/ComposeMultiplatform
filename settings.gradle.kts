rootProject.name = "Compose Playground"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://androidx.dev/storage/compose-compiler/repository/")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    val kotlinVersion = "1.7.20" // 1.6.21 // 1.7.0
    val agpVersion = "7.2.2" // 7.0.2 // 7.1.3 // 7.2.1
    // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
    val composeVersion = "1.3.0-beta03" // 1.2.0-alpha01-dev724

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("android") version kotlinVersion
        id("com.android.application") version agpVersion
        id("com.android.library") version agpVersion
        id("org.jetbrains.compose") version composeVersion
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}

include(
    ":core",
    ":ui:android",
    ":ui:desktop",
    ":ui:common",
)
