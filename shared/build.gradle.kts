plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    compileSdk = Sdk.compile

    defaultConfig {
        minSdk = Sdk.min
        targetSdk = Sdk.target

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":model"))
    implementation(Library.core)
    implementation(Library.appcompat)
    implementation(Library.material)
    implementation(Library.constraintLayout)
    implementation(Library.fragment)
    implementation(Library.hilt)
    kapt(Library.hiltCompiler)
    implementation(Library.dataStore)
    implementation(Library.retrofit)
    implementation(Library.retrofitConverterMoshi)
    implementation(Library.retrofitConverterScalars)
    implementation(Library.okhttp)
    implementation(Library.okhttpLoggingInterceptor)
    implementation(Library.moshi)
    implementation(Library.moshiKotlin)
    implementation(Library.networkResponse)
    implementation(Library.timber)
    testImplementation(Library.junit)
    androidTestImplementation(Library.testExtJunit)
    androidTestImplementation(Library.espressoCore)
}
