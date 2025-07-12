package com.subaiqiao.androidutils.modules.app.transactionEdit.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val id: Long,

    val date: String, // DATE
    val amount: Double, // REAL
    val type: String, // 'income' or 'expense'
    val description: String?,
    val category_id: Long?,
    val reference_transaction_id: Long?,
    val income_platform_id: Long?,
    val income_account_id: Long?,
    val expense_platform_id: Long?,
    val expense_account_id: Long?,
    val upload_status: Int = 0,
    val download_status: Int = 0,
    val created_at: String? = null,
    val updated_at: String? = null
)
