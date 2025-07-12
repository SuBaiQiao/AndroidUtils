package com.subaiqiao.androidutils.modules.app.transactionEdit.repository

import com.subaiqiao.androidutils.modules.app.transactionEdit.dao.TransactionDao
import com.subaiqiao.androidutils.modules.app.transactionEdit.entity.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    suspend fun insert(transaction: Transaction) = transactionDao.insert(transaction)
    suspend fun update(transaction: Transaction) = transactionDao.update(transaction)

    fun getTransactionsByDate(date: String): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDate(date)

    suspend fun getUnuploadedTransactions(): Flow<List<Transaction>> =
        transactionDao.getUnuploadedTransactions()

    suspend fun markAsUploaded(ids: List<Long>) =
        transactionDao.markAsUploaded(ids)
}