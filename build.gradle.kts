plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    id("io.gitlab.arturbosch.detekt") version BuildPluginsVersion.deteKt
    id("org.jlleitschuh.gradle.ktlint") version BuildPluginsVersion.ktLint
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${BuildPluginsVersion.secrets}")
        classpath("com.android.tools.build:gradle:${BuildPluginsVersion.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildPluginsVersion.kotlin}")
        classpath("com.google.gms:google-services:${BuildPluginsVersion.gms}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${BuildPluginsVersion.safeArgs}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${BuildPluginsVersion.crashlytics}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${BuildPluginsVersion.hilt}")
    }
}

allprojects {
    group = publishingGroup
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    ktlint {
        debug.set(false)
        version.set(Version.ktLint)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        config = rootProject.files("config/detekt/detekt.yml")
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
        }
    }
}

tasks {
    register("clean", Delete::class.java){
        delete(rootProject.buildDir)
    }
}