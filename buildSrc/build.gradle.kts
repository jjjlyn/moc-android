
plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

object Version {
    const val kotlin = "1.5.21"
    const val gradle = "7.0.3"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
    implementation("com.android.tools.build:gradle:${Version.gradle}")
}