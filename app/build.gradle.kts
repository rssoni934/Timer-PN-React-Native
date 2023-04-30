/*
 * Designed and developed by Imran
 */
import com.imranhsn.notificationtimer.Configuration
import com.android.build.api.dsl.ManagedVirtualDevice

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.hilt.plugin.get().pluginId)
}

android {
    compileSdk = Configuration.compileSdk
    defaultConfig {
        applicationId = "com.imranhsn.notificationtimer"
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }

    hilt {
        enableAggregatingTask = true
    }

    kotlin {
        sourceSets.configureEach {
            kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = getByName("debug").signingConfig
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    // androidx
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.splashscreen)

    // di
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.compiler)

    // unit test
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(libs.android.test.runner)
}
