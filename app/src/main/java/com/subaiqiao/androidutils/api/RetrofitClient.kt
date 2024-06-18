package com.subaiqiao.androidutils.api

import com.subaiqiao.androidutils.constant.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 设置日志级别，仅在调试时使用
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // 添加日志拦截器
        .build()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
        .client(okHttpClient) // 设置OkHttp客户端
        .build()

    fun buildRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
            .client(okHttpClient) // 设置OkHttp客户端
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    var apiService: ApiService = retrofit.create(ApiService::class.java)
}