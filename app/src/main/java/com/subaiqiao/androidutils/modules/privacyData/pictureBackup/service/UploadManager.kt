package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.service

import android.content.Context
import androidx.work.*
import java.io.File
import java.util.UUID

class UploadManager(private val context: Context) {

    fun enqueueUpload(file: File): UUID {
        val uploadRequest = createUploadRequest(file)
        WorkManager.getInstance(context).enqueue(uploadRequest)
        return uploadRequest.id // 返回系统生成的 UUID
    }

    fun observeUploadStatus(id: UUID, observer: (WorkInfo) -> Unit) {
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(id)
            .observeForever(observer)
    }

    companion object {
        fun createUploadRequest(file: File): OneTimeWorkRequest {
            val data = workDataOf(UploadWorker.KEY_FILE_PATH to file.absolutePath)
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
}

