package com.subaiqiao.androidutils.modules.app.transactionEdit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.subaiqiao.androidutils.modules.app.transactionEdit.dao.TransactionDao
import com.subaiqiao.androidutils.modules.app.transactionEdit.entity.Transaction


@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "androidUtils"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}