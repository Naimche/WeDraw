plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id ("com.google.gms.google-services")
}


android {
    namespace = "com.bupware.wedraw.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.bupware.wedraw.android"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.activity:activity-compose:1.7.2")

    //Coil - AsyncImage
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil:2.2.2")

    //SystemBarController
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.8.22")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.room:room-common:2.5.2")
    kapt("com.google.dagger:hilt-android-compiler:2.45")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    //ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //Kotlin coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    //Librerias
    implementation("io.ak1:drawbox:1.0.3")

    //Google Services
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    //Glance Widget
    // For Glance support
    implementation ("androidx.glance:glance:1.0.0-beta01")
    // For AppWidgets support
    implementation ("androidx.glance:glance-appwidget:1.0.0-beta01")

    // For Wear-Tiles support
    implementation ("androidx.glance:glance-wear-tiles:1.0.0-alpha05")

    //Room Components
    implementation("androidx.room:room-ktx:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-runtime:2.5.2")


    //Coil - AsyncImage
    implementation("io.coil-kt:coil-compose:2.2.2")


    //Corutines WorkManager
    // Dependencia de WorkManager
    implementation ("androidx.work:work-runtime-ktx:2.7.0")

    // Dependencia de Kotlin Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")

    //Gson
    implementation ("com.google.code.gson:gson:2.9.0")

    //Color Wheel
    implementation("com.godaddy.android.colorpicker:compose-color-picker:0.7.0")
    implementation("com.godaddy.android.colorpicker:compose-color-picker-android:0.7.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")



}