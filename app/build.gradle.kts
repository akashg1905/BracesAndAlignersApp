import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
val hasReleaseKeystore = keystorePropertiesFile.exists()
if (hasReleaseKeystore) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.smylo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.smylo"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "0.1.0-beta.2"
    }

    signingConfigs {
        if (hasReleaseKeystore) {
            create("release") {
                val storePath = keystoreProperties.getProperty("storeFile")
                    ?: error("keystore.properties missing storeFile")
                storeFile = rootProject.file(storePath)
                storePassword = keystoreProperties.getProperty("storePassword")
                    ?: error("keystore.properties missing storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                    ?: error("keystore.properties missing keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                    ?: error("keystore.properties missing keyPassword")
            }
        }
    }

    val apiUrl = project.findProperty("API_BASE_URL") ?: "http://10.0.2.2:8000/"
    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"$apiUrl\"")
        }
        release {
            // Prefer production URL from gradle.properties; never fall back to emulator HTTP.
            val releaseApiUrl = project.findProperty("API_BASE_URL")
                ?.toString()
                ?.takeIf { it.startsWith("https://") }
                ?: "https://bracesandalignersbackend.onrender.com/"
            buildConfigField("String", "API_BASE_URL", "\"$releaseApiUrl\"")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasReleaseKeystore) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons.extended)
    implementation(libs.android.material)
    implementation(libs.coil.compose)

    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)
    implementation(libs.camerax.extensions)

    implementation(libs.accompanist.permissions)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.work)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.work.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.graphics.path)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.firebase.messaging)

    testImplementation(libs.junit4)
}
