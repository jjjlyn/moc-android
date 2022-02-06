object Sdk {
    const val min = 23
    const val compile = 31
    const val target = compile
}

object Version {
    // androidX
    const val activity = "1.2.0-alpha06"
    const val appCompat = "1.3.0"
    const val core = "1.6.0"
    const val ktLint = "0.42.0"
    const val lifecycle = "2.4.0-alpha01"
    const val fragment = "1.3.0"
    const val dataStore = "1.0.0-beta01"
    const val pref = "1.1.1"
    const val navigation = "2.3.5"
    const val paging = "3.1.0"

    const val kotlinSerialization = "1.3.1"
    const val kotlinSerializationConverter = "0.8.0"

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
    const val networkResponse = "4.2.2"

    // test
    const val androidTest = "1.4.0"
    const val androidTestExt = "1.1.3"
    const val junit = "4.13.2"
    const val junitExt = "1.1.2"
    const val espresso = " 3.3.0"
    const val espressoCore = "3.4.0"

    // di
    const val hilt = "2.36"
    const val hiltJetpack = "1.0.0-alpha01"
    const val hiltNavigation ="1.0.0-alpha03"

    //util
    const val  timber = "4.7.1"
}

object BuildPluginsVersion {
    const val kotlin = "1.4.32"
    const val gradle = "7.0.3"

    // lint
    const val deteKt = "1.17.1"
    const val ktLint = "10.1.0"
    const val secrets = "2.0.0"

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
    const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinSerialization}"
    const val kotlinSerializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Version.kotlinSerializationConverter}"

    // androidx
    const val activity = "androidx.activity:activity-ktx:${Version.activity}"
    const val core = "androidx.core:core-ktx:${Version.core}"
    const val appcompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val dataStore = "androidx.datastore:datastore-preferences:${Version.dataStore}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Version.lifecycle}"
    const val lifecycleRunTime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val fragment = "androidx.fragment:fragment-ktx:${Version.fragment}"
    const val pref = "androidx.preference:preference-ktx:${Version.pref}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    const val paging = "androidx.paging:paging-runtime:${Version.paging}"

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
    const val retrofitConverterScalars = "com.squareup.retrofit2:converter-scalars:${Version.retrofit}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Version.okhttp}}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"
    const val networkResponse = "com.github.haroldadmin:NetworkResponseAdapter:${Version.networkResponse}"
    const val moshi = "com.squareup.moshi:moshi:${Version.moshi}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Version.moshi}"

    // util
    const val timber = "com.jakewharton.timber:timber:${Version.timber}"

    // test
    const val testRules = "androidx.test:rules:${Version.androidTest}"
    const val testRunner = "androidx.test:runner:${Version.androidTest}"
    const val junit = "junit:junit:${Version.junit}"
    const val testExtJunit = "androidx.test.ext:junit:${Version.androidTestExt}"
//    const val testExtJunitKtx = "androidx.test.ext:junit-ktx:${Version.androidTestExt}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Version.espressoCore}"

    // hilt
    const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
}