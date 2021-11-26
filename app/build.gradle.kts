plugins {
    id("com.android.application")
    kotlin("android")
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
    implementation(project(":core"))
    implementation(Library.coreKtx)
    implementation(Library.appcompat)
    implementation(Library.material)
    implementation(Library.constraintLayout)
    implementation(Library.hilt)
//    implementation(Library.hiltCompiler)
    testImplementation(Library.junit)
    androidTestImplementation(Library.testExtJunit)
    androidTestImplementation(Library.espressoCore)
}
