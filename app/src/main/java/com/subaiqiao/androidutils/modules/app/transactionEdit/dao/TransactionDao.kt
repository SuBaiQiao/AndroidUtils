package com.subaiqiao.androidutils.modules.app.transactionEdit.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.subaiqiao.androidutils.modules.app.transactionEdit.entity.Transaction

@Dao
interface TransactionDao {

    // 插入单条记录
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    // 更新单条记录
    @Update
    suspend fun update(transaction: Transaction)

    // 查询某天的所有交易记录（Flow 支持）
    @Query("SELECT * FROM transactions WHERE date = :date")
    fun getTransactionsByDate(date: String): Flow<List<Transaction>>

    // 获取所有未上传的记录（Flow 支持）
    @Query("SELECT * FROM transactions WHERE upload_status = 0")
    fun getUnuploadedTransactions(): Flow<List<Transaction>>

    // 标记为已上传
    @Query("UPDATE transactions SET upload_status = 1 WHERE id IN (:ids)")
    suspend fun markAsUploaded(ids: List<Long>): Int
}