import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.bitpanda.livechallenge"
    compileSdk = 36

    defaultConfig {
        namespace = "com.bitpanda.livechallenge"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val localProperties = gradleLocalProperties(rootDir, providers)
        val apiKey: String = localProperties.getProperty("api_key")
        buildConfigField("String", "api_key", value = "\"${apiKey}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Support libs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.material3)
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)

    // Network
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    implementation(libs.arrow.core)
    ksp(libs.moshi.kotlin.codegen)

    // Coroutines
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    // testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:5.0.5")
    ksp("com.github.bumptech.glide:compiler:5.0.5")

    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito:4.8.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation(libs.mockk)

    debugImplementation(libs.androidx.ui.tooling)
}