package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.byren.kai.thinkdaily.beans.Quick
import com.byren.kai.thinkdaily.beans.Remind

@SuppressLint("StaticFieldLeak")
object quickDbManager {
    private val mContext = Common.mContext()
    private var quickSql: SQLiteDatabase = mContext.openOrCreateDatabase("thinkDaily.db", Context.MODE_PRIVATE, null)

    fun getQuickDb(): ArrayList<Quick> {
        val list: ArrayList<Quick> = ArrayList()
        val cursor: Cursor = quickSql.rawQuery("select * from quick", null, null)
        while (cursor.moveToNext()) {
            val quick = Quick(cursor.getInt(0), cursor.getString(1))
            Log.d("id", "" + quick)
            list.add(quick)
        }
        cursor.close()
        return list
    }

    @SuppressLint("Recycle")
    fun deleteQuickDb(id: Int) : Boolean {
        try {
//            quickSql.delete("quick","_id=?", arrayOf(id.toString()))
            quickSql.execSQL("delete from quick where _id =$id")
            return true
        }catch (e: SQLiteConstraintException){
            return false
        }
    }

    fun addQuick(content: String): Quick {
        val quick = Quick(0, content)
        val values = ContentValues()
        values.put("content", content)
        quickSql.insert("quick", null, values)
        return quick
    }
}