plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.subaiqiao.androidutils"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.subaiqiao.androidutils"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit) // 使用最新版本
    implementation(libs.okhttp) // 使用最新版本
    // 如果你需要JSON转换，可以添加Gson或Moshi等库
    implementation(libs.converter.gson) // 使用Gson
    // 如果你需要处理网络请求的日志，可以添加OkHttp的日志拦截器
    implementation(libs.logging.interceptor)

    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-java:v8.6.0-release-jitpack")

    //是否需要ExoPlayer模式
    implementation("com.github.CarGuo.GSYVideoPlayer:GSYVideoPlayer-exo2:v8.6.0-release-jitpack")

    //是否需要AliPlayer模式
    implementation("com.github.CarGuo.GSYVideoPlayer:GSYVideoPlayer-aliplay:v8.6.0-release-jitpack")

    //根据你的需求ijk模式的so
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-arm64:v8.6.0-release-jitpack")
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-armv7a:v8.6.0-release-jitpack")
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-armv5:v8.6.0-release-jitpack")
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-x86:v8.6.0-release-jitpack")
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-x64:v8.6.0-release-jitpack")

    // 权限请求框架：https://github.com/getActivity/XXPermissions
    implementation("com.github.getActivity:XXPermissions:18.63")
}