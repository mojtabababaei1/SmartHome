plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    // Remove compose-compiler plugin
}

android {
    namespace = "com.maadiran.myvision"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maadiran.myvision"  // Changed from "com.maadiran.myvision.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            abiFilters.clear()  // Clear existing filters
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64"))
        }
        packaging {
            jniLibs.pickFirsts.add("lib/x86/libjnidispatch.so")
            jniLibs.pickFirsts.add("lib/x86_64/libjnidispatch.so")
            jniLibs.pickFirsts.add("lib/armeabi-v7a/libjnidispatch.so")
            jniLibs.pickFirsts.add("lib/arm64-v8a/libjnidispatch.so")
        }
        resourceConfigurations.addAll(listOf("en", "fa"))
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn"  // Changed from -Xopt-in
        )
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true  // Add this line
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            pickFirsts += setOf(
                "META-INF/versions/9/previous-compilation-data.bin",
                "lib/**/libjnidispatch.so"
            )
            pickFirsts += listOf(
                "lib/**/libjnidispatch.so",
                "lib/arm64-v8a/libjnidispatch.so",
                "lib/armeabi-v7a/libjnidispatch.so",
                "lib/x86/libjnidispatch.so",
                "lib/x86_64/libjnidispatch.so"
            )
            excludes += listOf(
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt",
                "META-INF/DEPENDENCIES",
                "**/attach_hotspot_windows.dll"
            )
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    ndkVersion = "25.2.9519653"  // Add specific NDK version
}

dependencies {
    // Update desugar version and move it to the top
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core"))
    implementation(project(":presentation"))

    // Update Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    
    // AndroidX and Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)  // Changed from libs.appcompat
    // implementation(libs.rendering)
    // Not directly used in provided files
    // implementation(libs.androidx.transition.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

    // Testing dependencies - keep for testing purposes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Security - used in the app
    implementation(libs.bcprov.jdk15on)
    implementation(libs.bcpkix.jdk15on)

    // Network and Data - essential for JMDNS and logging
    implementation(libs.jmdns)
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)

    // Coroutines - used extensively
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Retrofit - used in network module
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)

    // Not directly used in provided files
    // implementation(libs.kotlinx.serialization.json)

    // Not directly used in provided files
    // implementation(libs.accompanist.swiperefresh)

    // Used in RefrigeratorMonitoringWorker
    implementation(libs.androidx.work.runtime.ktx)

    implementation("com.google.protobuf:protobuf-java:3.21.12")
    implementation ("org.jmdns:jmdns:3.5.7")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(libs.dagger.hilt.android)
    kapt(libs.hilt.compiler)

    // Add explicit JNA dependencies at app level
    implementation("net.java.dev.jna:jna:5.13.0") {
        exclude(group = "org.apache.directory.studio", module = "org.apache.commons.io")
    }
    // Remove or comment out jna-platform
    // implementation("net.java.dev.jna:jna-platform:5.13.0")

    // Update JNA dependency to explicitly include natives
    // implementation("net.java.dev.jna:jna:5.13.0@aar") {
    //     isTransitive = true
    // }
    //chart
    implementation("com.patrykandpatrick.vico:compose:1.13.0")
    implementation("com.patrykandpatrick.vico:compose-m3:1.13.0")
    implementation("com.patrykandpatrick.vico:core:1.13.0")

    // (اختیاری) MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}

kapt {
    correctErrorTypes = true
    arguments {        arg("kapt.kotlin.generated", file("build/generated/kaptKotlin/").toString())    }}