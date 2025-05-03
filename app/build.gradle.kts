plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ptitdelivery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ptitdelivery"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit và Gson Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp Logging Interceptor (giúp debug API dễ dàng)
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation ("io.socket:socket.io-client:2.0.1")
    implementation ("org.osmdroid:osmdroid-android:6.1.16")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("androidx.biometric:biometric:1.1.0")

}