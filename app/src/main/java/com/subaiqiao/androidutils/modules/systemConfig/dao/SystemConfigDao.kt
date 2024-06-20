package com.subaiqiao.androidutils.modules.systemConfig.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.subaiqiao.androidutils.modules.systemConfig.entity.SystemConfig
import com.subaiqiao.androidutils.utils.db.DbHelper
import com.subaiqiao.androidutils.utils.db.Tables
import java.util.ArrayList

class SystemConfigDao(context: Context) {
    private val dbHelper = DbHelper(context, "androidUtils.db", 1)
    private lateinit var db: SQLiteDatabase

    fun openDB() {
        println("打开数据库连接")
        db = dbHelper.writableDatabase
        if (dbHelper.doesTableExist(db, "t_system_config")) {
            println("存在表")
        } else {
            println("不存在表")
            db.execSQL(Tables.CREATE_SYSTEM_CONFIG_TABLE)
        }
    }

    fun close() {
        println("关闭数据库连接")
        if (db != null) {
            db.close()
        }
    }

    fun insert(systemConfig: SystemConfig): Long {
        println("准备插入数据：" + systemConfig.code + "\t" + systemConfig.value)
        val values = ContentValues().apply {
            put("id", systemConfig.id)
            put("code", systemConfig.code)
            put("value", systemConfig.value)
            put("is_delete", systemConfig.isDelete)
        }
        return db.insert("t_system_config", null, values)
    }

    fun deleteById(id: String): Int {
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
        println("准备查询")
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
                println(systemConfig.id + "\t" + systemConfig.code + "\t" + systemConfig.value)
            } while (cursor.moveToNext())
        } else {
            println("无数据")
        }
        cursor.close()
        return result
    }

}