package com.subaiqiao.androidutils.api

import retrofit2.http.GET

interface ApiService {
    @GET("lockScreen") // 假设API的端点是/lockScreen
    suspend fun lockScreen(): Result // 使用Kotlin协程的suspend函数
}