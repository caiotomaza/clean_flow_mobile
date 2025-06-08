plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.unifapce.clean_flow"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.unifapce.clean_flow"
        minSdk = 33
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildToolsVersion = "36.0.0"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
        implementation("com.squareup.retrofit2:retrofit:2.9.0") // Dependecias necessarias para converter o json que o laravel manda;
        implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Dependecias necessarias para converter o json que o laravel manda;
        implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0") // Para logs de rede (opcional, mas muito útil para debug)
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Coroutines para operações assíncronas
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Coroutines para operações assíncronas
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
}