plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Sdk.compile

    defaultConfig {
        minSdk = Sdk.min
        targetSdk = Sdk.target

        applicationId = AppCoordinates.appID
        versionCode = AppCoordinates.appVersionCode
        versionName = AppCoordinates.appVersionName

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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":model"))
    implementation(Library.kotlinLib)
    implementation(Library.activity)
    implementation(Library.appcompat)
    implementation(Library.core)
    implementation(Library.material)
    implementation(Library.constraintLayout)
    implementation(Library.fragment)
    implementation(Library.hilt)
    kapt(Library.hiltCompiler)
    implementation(Library.dataStore)
    implementation(Library.okhttp)
    implementation(Library.okhttpLoggingInterceptor)
    implementation(Library.retrofit)
    implementation(Library.retrofitConverterMoshi)
    implementation(Library.retrofitConverterScalars)
    implementation(Library.moshi)
    implementation(Library.moshiKotlin)
    implementation(Library.lifecycleCompiler)
    implementation(Library.lifecycleRunTime)
    implementation(Library.navigationFragment)
    implementation(Library.navigationUI)
    implementation(Library.paging)
    implementation(Library.timber)
    testImplementation(Library.junit)
    androidTestImplementation(Library.testExtJunit)
    androidTestImplementation(Library.espressoCore)
}
