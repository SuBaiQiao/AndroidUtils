package com.subaiqiao.androidutils.modules.app.transactionEdit.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey
    val id: Long,

    val name: String
)
