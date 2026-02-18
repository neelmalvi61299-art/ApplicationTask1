plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-android")
   // id("com.google.gms.google-services")
}

android {
    namespace = "com.example.practicletask"
    compileSdk = 36
    buildFeatures {
        //dataBinding = true
        viewBinding = true // if you also want viewBinding
    }

    defaultConfig {
        applicationId = "com.example.practicletask"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 36
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

    packaging {
        resources {
            excludes.add("/META-INF/gradle/incremental.annotation.processors")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1") // DOWNGRADED
    implementation("com.google.android.material:material:1.11.0") // DOWNGRADED
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // DOWNGRADED
    implementation("androidx.activity:activity:1.8.0") // DOWNGRADED
    //  implementation("com.google.firebase:firebase-messaging-ktx:25.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // DOWNGRADED
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // DOWNGRADED

    implementation("com.google.dagger:hilt-android:2.55")
    implementation("com.google.dagger:hilt-android-compiler:2.55")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.activity:activity-ktx:1.9.2")


// Converter (Gson for JSON)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp (HTTP client used by Retrofit)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

// OkHttp Logging Interceptor (for debugging network calls)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // scalable size unit
    implementation ("com.intuit.ssp:ssp-android:1.1.0")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    implementation ("com.airbnb.android:lottie:5.2.0")


    implementation(project(":network-module"))
    implementation(project(":core-module"))

    implementation("com.github.bumptech.glide:glide:4.16.0")
  //  kapt("com.github.bumptech.glide:compiler:4.16.0")


    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-messaging-ktx")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")


}