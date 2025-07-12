plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt") // 启用 kapt
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
        // Room 配置
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
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
        viewBinding = true
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
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.preference)
//    implementation(libs.androidx.preference.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    debugImplementation(libs.androidx.appcompat.v170)
    debugImplementation(libs.androidx.preference.ktx)

    implementation(libs.retrofit) // 使用最新版本
    implementation(libs.okhttp) // 使用最新版本
    // 如果你需要JSON转换，可以添加Gson或Moshi等库
    implementation(libs.converter.gson) // 使用Gson
    // 如果你需要处理网络请求的日志，可以添加OkHttp的日志拦截器
    implementation(libs.logging.interceptor)

    implementation(libs.gsyvideoplayer.java)

    //是否需要ExoPlayer模式
    implementation(libs.gsyvideoplayer.exo2)

    //是否需要AliPlayer模式
//    implementation("com.shuyu:GSYVideoPlayer-aliplay:8.1.2")

    //根据你的需求ijk模式的so
    implementation(libs.gsyvideoplayer.arm64)
    implementation(libs.gsyvideoplayer.armv7a)
    implementation(libs.gsyvideoplayer.armv5)
    implementation(libs.gsyvideoplayer.x86)
    implementation(libs.gsyvideoplayer.x64)

    // 权限请求框架：https://github.com/getActivity/XXPermissions
    implementation(libs.xxpermissions)
    implementation(libs.lfilepickerlibrary)

    implementation(libs.roughike.bottom.bar)
    // 图片展示框架：https://github.com/bumptech/glide
    implementation(libs.github.glide)
    annotationProcessor(libs.compiler)

    // 日志框架
    implementation(libs.utilcodex)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.androidx.work.runtime.ktx)

    // Room Database
    implementation(libs.androidx.room.runtime)
    // Room with Kotlin Extensions (for coroutines & flow)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

// 留一个，怕忘记怎么写
//    implementation("androidx.activity:activity-ktx:1.7.1")
}