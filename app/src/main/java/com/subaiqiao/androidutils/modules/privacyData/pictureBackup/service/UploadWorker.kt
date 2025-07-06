package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.service

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class UploadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val KEY_FILE_PATH = "file_path"

        fun createUploadRequest(file: File): OneTimeWorkRequest {
            val data = workDataOf(KEY_FILE_PATH to file.absolutePath)
            return OneTimeWorkRequestBuilder<UploadWorker>()
                .setInputData(data)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val filePath = inputData.getString(KEY_FILE_PATH)
        if (filePath == null) {
            return@withContext Result.failure(workDataOf("error" to "文件路径为空"))
        }

        val file = File(filePath)
        if (!file.exists()) {
            return@withContext Result.failure(workDataOf("error" to "文件不存在"))
        }

        try {
            // 调用你的上传服务
            val success = UploadService.uploadFileInForeground(file)

            if (success) {
                Result.success()
            } else {
                Result.retry() // 或者 Result.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(workDataOf("error" to "未知错误"))
        }
    }
}
