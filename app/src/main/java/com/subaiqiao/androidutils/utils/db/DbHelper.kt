package com.subaiqiao.androidutils.utils.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context, name: String, version: Int) : SQLiteOpenHelper (context, name, null, version){

    override fun onCreate(db: SQLiteDatabase?) {
        println("创建数据库")
        db?.execSQL(Tables.CREATE_SYSTEM_CONFIG_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun doesTableExist(db: SQLiteDatabase, tableName: String): Boolean {
        if (db.isOpen) {
            var cursor: Cursor? = null
            try {
                cursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                    arrayOf(tableName)
                )
                if (cursor != null && cursor.moveToFirst()) {
                    return true // 表存在
                }
            } finally {
                cursor?.close()
            }
        }
        return false // 表不存在
    }
}