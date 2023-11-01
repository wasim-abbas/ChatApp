plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.wasim.csdl.patient_chatapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wasim.csdl.patient_chatapp"
        minSdk = 23
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
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")

    //SDP
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    //SSP
    implementation ("com.intuit.ssp:ssp-android:1.1.0")

    //Navigation Component
    implementation ("android.arch.navigation:navigation-fragment-ktx:1.0.0")
    implementation ("android.arch.navigation:navigation-ui-ktx:1.0.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Okhttp logging intercepter
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //LifeCycle
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //KProgressHud
    implementation ("io.github.rupinderjeet:kprogresshud:1.0.0")

    //Paper Database
    implementation("io.github.pilgr:paperdb:2.7.1")

    // for ripple effect
    implementation ("com.balysv:material-ripple:1.0.2")

    //CalendarView
   // implementation ("com.github.kizitonwose:CalendarView:0.4.0")

    ///Firebase
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.firebase:firebase-auth:22.1.2")
    implementation ("com.google.firebase:firebase-storage-ktx:20.2.1")
    implementation ("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation ("com.google.firebase:firebase-messaging:22.0.0")

    //other
    implementation ("de.hdodenhof:circleimageview:2.2.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.rengwuxian.materialedittext:library:2.1.4")



}