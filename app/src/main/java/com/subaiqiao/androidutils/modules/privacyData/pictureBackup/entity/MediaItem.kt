package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity

import android.net.Uri

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val dateAddedMillis: Long, // 时间戳用于排序
    val date: String,          // 显示用
    val mimeType: String,
    val isVideo: Boolean = mimeType.startsWith("video/"),
    var selected: Boolean = false, // 新增字段
    // 新增上传进度字段
    var uploadProgress: Int = 0,
    // 新增上传状态字段
    var uploadStatus: UploadStatus = UploadStatus.NotStarted
)

enum class UploadStatus {
    NotStarted,   // 未开始
    Uploading,    // 上传中
    Completed,    // 已完成
    Failed        // 失败
}

