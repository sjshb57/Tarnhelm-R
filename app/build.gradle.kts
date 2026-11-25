@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "cn.ac.lz233.tarnhelm"
    compileSdk {
        version = release(37) {
            minorApiLevel = 2
        }
    }

    defaultConfig {
        applicationId = "cn.ac.lz233.tarnhelm"
        minSdk {
            version = release(27)
        }
        targetSdk {
            version = release(37)
        }
        versionCode = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toInt()
        versionName = "1.8.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("github") {
            dimension = "version"
            buildConfigField("String", "FLAVOR", "\"github\"")
        }
        create("google") {
            dimension = "version"
            buildConfigField("String", "FLAVOR", "\"google\"")
        }
        create("fdroid") {
            dimension = "version"
            buildConfigField("String", "FLAVOR", "\"fdroid\"")
        }
        create("coolapk") {
            dimension = "version"
            buildConfigField("String", "FLAVOR", "\"coolapk\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        aidl = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/**",
                "kotlin/**",
                "okhttp3/**",
                "org/**",
                "**.properties",
                "**.bin",
                "**kotlin**",
            )
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

configurations.configureEach {
    exclude(group = "dev.rikka.rikkax.appcompat", module = "appcompat")
}

dependencies {
    implementation(project(":shizuku-service"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.window)
    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.material)
    implementation(libs.rikka.material.preference)

    implementation(libs.permissionx)

    implementation(libs.okhttp)

    compileOnly(libs.xposed.api)

    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)

    implementation(libs.hiddenapibypass)

    implementation(libs.fastkv)
    implementation(libs.packable.kotlin)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
