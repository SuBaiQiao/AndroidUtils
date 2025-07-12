package com.subaiqiao.androidutils.modules.app.transactionEdit.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: Long,

    val name: String,
    val parent_id: Long?,
    val type: String // 'income' or 'expense'
)
