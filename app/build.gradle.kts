import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.isIncludeCompileClasspath

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.sonarqube")
    id("jacoco")
}

android {
    namespace = "com.silverst.kpitassignment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.silverst.kpitassignment"
        minSdk = 24
        targetSdk = 34
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

        debug {
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.7.1")
    //classpath "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.6.1"


    // AndroidX Core Testing for InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Kotlin Coroutines Testing for handling coroutines in tests
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    //To test stateFlow emmission
    testImplementation("app.cash.turbine:turbine:1.0.0")


    // Mocking framework (optional, but useful for mocking dependencies)
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Compose testing dependencies
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")

    implementation("org.jacoco:org.jacoco.core:0.8.11")
}

jacoco {
    toolVersion = "0.8.11" // Use the latest version
    reportsDirectory.set(layout.buildDirectory.dir("reports/jacoco"))
}


