package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

@SuppressLint("StaticFieldLeak")
object DbHelper {
    private val mContext = Common.mContext()
    private var helper: SqHelper? = null
    fun createTable() {
        helper = SqHelper(mContext, "think.db", null, 1)
        helper!!.writableDatabase
    }

    class SqHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase?) {
//            创建支出表
            db!!.execSQL("CREATE TABLE expend (_id INTEGER PRIMARY KEY AUTOINCREMENT, expend TEXT, expendAmount TEXT, expendDate TEXT, expendNote TEXT);")
//            创建收入表
            db.execSQL("CREATE TABLE income (_id INTEGER PRIMARY KEY AUTOINCREMENT, incomeType TEXT, incomeAmount TEXT, incomeDate TEXT, incomeNote TEXT);")
//            创建快速记账表
            db.execSQL("CREATE TABLE quick (_id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT);")
//            创建提醒表
            db.execSQL("CREATE TABLE remind (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, date,TEXT);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    fun closeDb() {
        helper!!.close()
    }


}