object Sdk {
    const val min = 23
    const val compile = 31
    const val target = compile
}

object Version {
    // androidX
    const val appCompat = "1.3.0"
    const val coreKtx = "1.6.0"
    const val ktLint = "0.42.0"
    const val appcompat = "1.2.0"
    const val lifecycleRuntime = "2.3.1"

    // ui
    const val material = "1.3.0"
    const val constraintLayout = "2.0.4"

    // image
    const val glide = "4.12.0"
    const val lottie ="3.6.1"

    // network
    const val retrofit ="2.9.0"
    const val moshi = "1.12.0"
    const val okhttp ="4.9.0"

    // test
    const val androidTest = "1.4.0"
    const val androidTestExt = "1.1.3"
    const val junit = "4.13.2"
    const val junitExt = "1.1.2"
    const val espresso = " 3.3.0"
    const val espressoCore = "3.4.0"

    // di
    const val hilt = "2.33-beta"
    const val hiltJetpack = "1.0.0-alpha01"
    const val hiltNavigation ="1.0.0-alpha03"

    //util
    const val  timber = "4.7.1"
}

object BuildPluginsVersion {
    const val kotlin = "1.5.21"
    const val gradle = "7.0.3"

    // lint
    const val deteKt = "1.17.1"
    const val ktLint = "10.1.0"

    // hilt
    const val hilt = Version.hilt

    // safe-args
    const val safeArgs = "2.3.5"

    // firebase
    const val crashlytics = "2.8.0"

    // gms
    const val gms = "4.3.10"

}

object Library {
    // kotlin
    const val kotlinLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildPluginsVersion.kotlin}"

    // androidx
    const val core = "androidx.core:core-ktx:${Version.coreKtx}"
    const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val lifecycleRunTime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleRuntime}"

    // ui
    const val material = "com.google.android.material:material:${Version.material}"

    // image
    const val glide = "com.github.bumptech.glide:glide:${Version.glide}"
    const val glideOkHttpIntegration = "com.github.bumptech.glide:okhttp3-integration:${Version.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Version.glide}"
    const val lottie = "com.airbnb.android:lottie:${Version.lottie}"

    // network
    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Version.okhttp}}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"

    // util
    const val timber = "com.jakewharton.timber:timber:${Version.timber}"
    const val moshi = "com.squareup.moshi:moshi:${Version.moshi}"

    // test
    const val testRules = "androidx.test:rules:${Version.androidTest}"
    const val testRunner = "androidx.test:runner:${Version.androidTest}"
    const val junit = "junit:junit:${Version.junit}"
    const val testExtJunit = "androidx.test.ext:junit:${Version.androidTestExt}"
    const val testExtJunitKtx = "androidx.test.ext:junit-ktx:${Version.androidTestExt}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Version.espressoCore}"

    // hilt
    const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"


}