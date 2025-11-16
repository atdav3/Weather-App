plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
}

dependencies {

    // Android Core
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Weather API and networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Location services
    implementation("com.google.android.gms:play-services-location:21.0.1")
    
    // CardView for modern UI
    implementation("androidx.cardview:cardview:1.0.0")
    
    // RecyclerView for forecast list
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    
    // Lottie for animations
    implementation("com.airbnb.android:lottie:6.1.0")
    
                // Google Maps and Weather Map
            implementation("com.google.android.gms:play-services-maps:18.2.0")
            implementation("com.google.maps.android:android-maps-utils:2.3.0")
            implementation("com.github.bumptech.glide:glide:4.16.0")
            annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
            
            // Charts for Weather Data
            implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}