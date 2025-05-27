import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.hilt.gradle.plugin)
        // Add JUnit 5 support
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.9.3.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

// Add this configuration for all modules
subprojects {
    afterEvaluate {
        plugins.withId("kotlin-kapt") {
            configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
                correctErrorTypes = true
                arguments {
                    // Remove all previous arguments and only keep necessary ones
                    arg("kapt.kotlin.generated", file("build/generated/kaptKotlin/").toString())
                }
            }
        }
        plugins.withId("com.android.library") {
            configure<LibraryExtension> {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                    // Remove global desugaring config
                }
            }
        }
        plugins.withId("com.android.application") {
            configure<ApplicationExtension> {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                    // Remove global desugaring config
                }
            }
        }
    }
}