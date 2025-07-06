package com.subaiqiao.androidutils.modules.systemConfig.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.subaiqiao.androidutils.modules.systemConfig.entity.SystemConfig
import com.subaiqiao.androidutils.utils.db.DbHelper
import com.subaiqiao.androidutils.utils.db.Tables

class SystemConfigDao(private var context: Context) {

    companion object {
        const val TAG = "数据库操作"
    }

    private var dbHelper = DbHelper(context, "androidUtils.db", 1)
    private lateinit var db: SQLiteDatabase

    fun openDB() {
        Log.d(TAG, "打开数据库连接")
        db = dbHelper.writableDatabase
        if (dbHelper.doesTableExist(db, "t_system_config")) {
            Log.d(TAG, "openDB: 数据库与数据表t_system_config已存在")
        } else {
            Log.d(TAG, "openDB: 数据库或数据表t_system_config不存在，开始创建")
            db.execSQL(Tables.CREATE_SYSTEM_CONFIG_TABLE)
        }
    }

    fun close() {
        Log.d(TAG, "准备关闭数据库连接")
        if (::db.isInitialized) {
            Log.d(TAG, "执行关闭数据库连接")
            db.close()
        }
    }

    fun insert(systemConfig: SystemConfig): Long {
        Log.d(TAG, "准备插入数据：${systemConfig.code}\t${systemConfig.value}")
        val values = ContentValues().apply {
            put("id", systemConfig.id)
            put("code", systemConfig.code)
            put("value", systemConfig.value)
            put("is_delete", systemConfig.isDelete)
        }
        return db.insert("t_system_config", null, values)
    }

    private fun deleteById(id: String): Int {
        return db.delete("t_system_config", "id=?", arrayOf(id))
    }

    fun delete(systemConfig: SystemConfig): Int {
        return deleteById(systemConfig.id)
    }

    fun update(systemConfig: SystemConfig): Int {
        val values = ContentValues().apply {
            put("code", systemConfig.code)
            put("value", systemConfig.value)
            put("is_delete", systemConfig.isDelete)
        }
        println("修改配置：" + systemConfig.code + "\t" + systemConfig.value)
        return db.update("t_system_config", values, "id=?", arrayOf(systemConfig.id))
    }

    @SuppressLint("Range")
    fun selectList(where: String, whereArgs: Array<String>): ArrayList<SystemConfig> {
        Log.d(TAG, "准备查询")
        val cursor = db.query("t_system_config", null,
            where, whereArgs, null, null, null)
        val result = ArrayList<SystemConfig>()
        if (cursor.moveToFirst()) {
            do {
                val systemConfig = SystemConfig(
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("code")),
                    cursor.getString(cursor.getColumnIndex("value")),
                    cursor.getInt(cursor.getColumnIndex("is_delete"))
                )
                result.add(systemConfig)
                Log.d(TAG, "数据查询结果: ${systemConfig.id}\t${systemConfig.code}\t${systemConfig.value}")
            } while (cursor.moveToNext())
        } else {
            Log.d(TAG, "无数据")
        }
        cursor.close()
        return result
    }

}