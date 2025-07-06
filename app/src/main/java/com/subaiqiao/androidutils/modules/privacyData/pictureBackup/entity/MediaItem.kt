package com.subaiqiao.androidutils.modules.privacyData.pictureBackup.entity

data class MediaItem(
    val id: Long,
    val uri: String,
    val displayName: String,
    val dateAddedMillis: Long, // 时间戳用于排序
    val date: String,          // 显示用
    val mimeType: String,
    val isVideo: Boolean = mimeType.startsWith("video/"),
    var selected: Boolean = false // 新增字段
)

