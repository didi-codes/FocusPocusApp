import java.util.Properties

val localProperties = Properties().apply {
    load(File(rootProject.rootDir, "local.properties").inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}


android {
    namespace = "com.productivitybandits.focuspocusapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.productivitybandits.focuspocusapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "FIREBASE_API_KEY", "\"${localProperties.getProperty("FIREBASE_API_KEY")}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "FIREBASE_API_KEY", "\"${localProperties.getProperty("FIREBASE_API_KEY")}\"")
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation (libs.androidx.cardview)
    implementation(libs.firebase.common.ktx)
    implementation(libs.protolite.well.known.types)
    implementation(libs.firebase.analytics)
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.database)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.google.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation ("com.google.firebase:firebase-appcheck:17.0.1")
    implementation (libs.firebase.appcheck.interop)
    implementation(libs.firebase.common.ktx)
    implementation(libs.protolite.well.known.types)
    implementation("com.google.firebase:firebase-analytics:21.3.0")
    implementation(libs.firebase.firestore.ktx)
    implementation ("com.google.android.gms:play-services-auth:21.0.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.airbnb.android:lottie:6.1.0")
}

