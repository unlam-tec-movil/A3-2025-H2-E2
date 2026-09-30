import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.kover)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.compose.compiler)
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.10"
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        languageVersion.set(KotlinVersion.KOTLIN_2_1)
        apiVersion.set(KotlinVersion.KOTLIN_2_1)
    }
}

android {
    namespace = "ar.edu.unlam.mobile.scaffolding"
    compileSdk = 36

    defaultConfig {
        applicationId = "ar.edu.unlam.mobile.scaffolding"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // Runner para tests instrumentados (Compose UI Test necesita AndroidJUnitRunner)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        // Configuración de API Keys
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { stream ->
                localProperties.load(stream)
            }
        }

        // API Key para Google Directions API
        val googleDirectionsApiKey = localProperties.getProperty("GOOGLE_DIRECTIONS_API_KEY") ?: "PLACEHOLDER_API_KEY"
        buildConfigField("String", "GOOGLE_DIRECTIONS_API_KEY", "\"$googleDirectionsApiKey\"")

        // API Key para Google Maps SDK
        val googleMapsApiKey = localProperties.getProperty("GOOGLE_MAPS_API_KEY") ?: "PLACEHOLDER_API_KEY"
        resValue("string", "google_maps_key", googleMapsApiKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

// Fuerza a todas las librerías a usar versiones compatibles
configurations.all {
    resolutionStrategy {
        force("androidx.test:core:1.5.0")
        force("androidx.test:runner:1.5.0")
        force("androidx.test:rules:1.5.0")
        force("androidx.test:storage:1.4.0")
        force("androidx.test:monitor:1.6.1")
        force("androidx.test.espresso:espresso-core:3.5.1")
    }
}

dependencies {
    // Base Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icon)
    implementation(libs.accompanist.systemuicontroller)

    // 👇 Dependencia directa para ui-text
    implementation("androidx.compose.ui:ui-text:1.7.0")
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.compose.ui.test.junit4)

    // Testing Unitario (JVM)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(kotlin("test"))

    // Testing Instrumentado (Android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4") // 👈 NECESARIO para createComposeRule y onNodeWithTag
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation("androidx.compose.ui:ui-test-manifest") // 👈 NECESARIO para onNodeWithTag

    // Hilt Testing
    kspAndroidTest(libs.google.dagger.hilt.android.compiler)
    androidTestImplementation(libs.google.dagger.hilt.android.testing)
    testImplementation(libs.google.dagger.hilt.android.testing)

    // Dagger + Hilt
    implementation(libs.google.dagger.hilt.android)
    implementation(libs.androidx.compose.ui.text)
    ksp(libs.google.dagger.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // AndroidX
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Corrutinas
    implementation(libs.kotlinx.coroutines.android)

    // Google Maps & Location
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    // Coil
    implementation(libs.coil.compose)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // Accompanist
    implementation(libs.accompanist.permissions)

    // Gson - JSON serialization/deserialization
    implementation(libs.gson)

    // Retrofit - HTTP client
    implementation(libs.retrofit)

    // Retrofit Gson Converter - Conecta Retrofit con Gson
    implementation(libs.retrofit.converter.gson)

    // OkHttp Loggin Interceptor - Para ver las peticiones HTTP en Logcat (para debug)
    implementation(libs.okhttp.logging.interceptor)

    // Google Maps Utils (para decodificar polylines)
    implementation(libs.maps.utils)
}
