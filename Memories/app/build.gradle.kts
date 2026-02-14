import shadow.bundletool.com.android.tools.r8.internal.vi

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "pt.ipt.dam2025.memories"
    compileSdk = 36

    defaultConfig {
        applicationId = "pt.ipt.dam2025.memories"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        // Se não estás a usar DataBinding (com tags <layout> no XML),
        // podes deixar apenas o viewBinding para ser mais rápido.
        dataBinding = true
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // --- RETROFIT (Para a tua API Flask) ---
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    // --- GOOGLE PLAY SERVICES (Para o GPS/LocationServices) ---
    // Essencial para resolver os erros do CameraFragment
    implementation("com.google.android.gms:play-services-location:21.3.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}