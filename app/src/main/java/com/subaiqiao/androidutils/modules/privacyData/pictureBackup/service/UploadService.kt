package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object UploadService {
    private val client = OkHttpClient()

    fun uploadFileWithProgress(
        file: File,
        onProgress: (Int) -> Unit,
        onComplete: (Boolean, String) -> Unit
    ) {
        val requestBody = object : RequestBody() {
            override fun contentType() = "multipart/form-data".toMediaType()

            override fun writeTo(sink: BufferedSink) {
                val length = file.length().toFloat()
                var uploaded = 0L
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

                FileInputStream(file).use { fis ->
                    var read: Int
                    while (fis.read(buffer).also { read = it } != -1) {
                        sink.write(buffer, 0, read)
                        uploaded += read.toLong()
                        val progress = ((uploaded / length) * 100).toInt()
                        onProgress(progress)
                    }
                }

                // 确保数据写入网络流
                sink.flush()
            }

        }

        val body = MultipartBody.Builder().apply {
            setType(MultipartBody.FORM)
            addFormDataPart("file", file.name, requestBody)
        }.build()

        val request = Request.Builder()
            .url("http://192.168.154.131:8080/api/file/upload")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    onComplete(true, "上传成功")
                } else {
                    onComplete(false, "上传失败: ${response.message}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                onComplete(false, "网络错误: ${e.message ?: "未知错误"}")
            }
        })
    }

    // 同步上传方法，供 WorkManager 使用
    suspend fun uploadFileInForeground(file: File): Boolean {
        return withContext(Dispatchers.IO) {
            val requestBody = object : RequestBody() {
                override fun contentType() = "multipart/form-data".toMediaType()

                override fun writeTo(sink: BufferedSink) {
                    val length = file.length().toFloat()
                    var uploaded = 0L
                    val buffer = ByteArray(8192)

                    FileInputStream(file).use { fis ->
                        var read: Int
                        while (fis.read(buffer).also { read = it } != -1) {
                            sink.write(buffer, 0, read)
                            uploaded += read
                            // 如果需要进度回调，可以使用 LocalBroadcastManager 发送广播
                        }
                    }
                    sink.flush()
                }
            }

            val body = MultipartBody.Builder().apply {
                setType(MultipartBody.FORM)
                addFormDataPart("file", file.name, requestBody)
            }.build()

            val request = Request.Builder()
                .url("http://192.168.154.131:8080/api/file/upload")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            response.isSuccessful
        }
    }
}

