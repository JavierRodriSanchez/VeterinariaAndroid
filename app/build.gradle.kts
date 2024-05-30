plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.veterinariav2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.veterinariav2"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //stripe
    implementation("com.stripe:stripe-android:20.44.0")

    //maps
    implementation ("androidx.activity:activity-compose:1.4.0")
    implementation ("com.google.maps.android:maps-ktx:3.1.0")


    //google maps
    implementation ("com.google.maps.android:maps-compose:4.4.1")
    implementation ("com.google.accompanist:accompanist-permissions:0.33.2-alpha")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")


    //Google Services
    implementation("com.google.android.gms:play-services-auth:20.4.1")

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    //Iconos Login
    implementation("androidx.compose.material:material-icons-extended:1.3.1")



    implementation ("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation ("com.github.kittinunf.fuel:fuel-json:2.3.1")
    //Stripe implementation
    implementation("com.stripe:stripe-android:20.44.0")
    implementation ("com.stripe:stripe-java:25.0.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0")

    implementation ("androidx.navigation:navigation-compose:2.4.0-alpha10")

    implementation ("com.stripe:stripe-android:16.11.0")
    implementation ("com.google.android.gms:play-services-wallet:19.1.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha01")

    implementation ("io.coil-kt:coil-compose:1.4.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation( "com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.4.3")
    implementation ("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.core:core-ktx:1.9.0")

    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}