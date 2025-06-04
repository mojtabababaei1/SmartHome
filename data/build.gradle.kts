plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    id("com.google.protobuf") version "0.9.4"
    jacoco
}

android {
    namespace = "com.maadiran.myvision.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
            }
        }
    }
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
        resources {
            pickFirsts += listOf(
                "lib/**/libjnidispatch.so",
                "lib/arm64-v8a/libjnidispatch.so",
                "lib/armeabi-v7a/libjnidispatch.so",
                "lib/x86/libjnidispatch.so",
                "lib/x86_64/libjnidispatch.so"
            )
        }
    }
}

tasks.withType<Test> {
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED")
    // Add this to ensure MockK works properly with JUnit
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        files(
            fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
                exclude("**/R.class", "**/R\$*.class")
            }
        )
    )
    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(fileTree(buildDir).include("**/*.exec"))
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Retrofit and Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Core dependencies
    implementation(libs.gson.v2101)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.vosk.android)
    // Remove JNA dependencies
    // implementation("net.java.dev.jna:jna:5.13.0@aar")
    implementation("net.java.dev.jna:jna:5.13.0") {
        exclude(group = "org.apache.directory.studio", module = "org.apache.commons.io")
    }
    // Keep only Vosk
    implementation("com.alphacephei:vosk-android:0.3.32+")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.protobuf.java)


    implementation("androidx.datastore:datastore-preferences:1.1.0")



    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")



    // Use direct implementation
    implementation("com.alphacephei:vosk-android:0.3.32+")

    // Ensure KeyStore dependencies are included if needed
    implementation("org.bouncycastle:bcprov-jdk15on:1.70") // Example for BouncyCastle

    // Testing
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)  // Make sure this is testRuntimeOnly
    testImplementation(libs.mockk.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.bcprov.jdk15on)
    testImplementation(libs.protobuf.java)
    testImplementation(libs.protobuf.kotlin)
    testImplementation(libs.slf4j.simple)

    // Android Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("kapt.kotlin.generated", file("build/generated/kaptKotlin/").toString())
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.12"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") { }
            }
        }
    }
}