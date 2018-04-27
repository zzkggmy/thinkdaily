package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.byren.kai.thinkdaily.beans.Remind

@SuppressLint("StaticFieldLeak")
object remindDbManager {
    private val mContext = Common.mContext()
    private val remindDb: RemindDb = RemindDb(mContext, "remind.db", null, 1)
    private var remindSql: SQLiteDatabase? = null

    init {
        remindDb.writableDatabase
        remindSql = mContext.openOrCreateDatabase("remind.db", Context.MODE_PRIVATE, null)
    }

    fun getRemindDb(): ArrayList<Remind> {
        val list: ArrayList<Remind> = ArrayList()
        val cursor: Cursor = remindSql!!.rawQuery("select * from remind", null, null)
        while (cursor.moveToNext()) {
            val remind: Remind = Remind(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
            list.add(remind)
        }
        cursor.close()
        return list
    }

    @SuppressLint("Recycle")
    fun deleteRemindDb(id: Int) : Boolean {
        remindDb.writableDatabase
        try {
            remindSql!!.execSQL("delete from remind where _id =$id")
            return true
        }catch (e: SQLiteConstraintException){
            return false
        }
    }

    fun addRemind(title: String, date: String): Boolean {
        val values = ContentValues()
        values.put("title", title)
        values.put("date", date)
        remindSql!!.insert("remind", null, values)
        return getRemindDb().size >= 1
    }

    fun conditionQuery(date: String): ArrayList<Remind> {

        val conditionList: ArrayList<Remind> = ArrayList()
        remindDb.writableDatabase
        remindSql = mContext.openOrCreateDatabase("remind.db", Context.MODE_PRIVATE, null)
        val cursor: Cursor = remindSql!!.rawQuery("select * from remind", null)
        while (cursor.moveToNext()) {
            if (cursor.getString(2) == date) {
                val remind = Remind(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
                conditionList.add(remind)
            }
        }
        return conditionList
    }

    fun conditionalQuery(date: String) : ArrayList<Remind>{
        val list: ArrayList<Remind> = ArrayList()
        val cursor: Cursor = remindSql!!.rawQuery("select * from remind",null,null)
        if (date.isNotEmpty()) {
            while (cursor.moveToNext()) {
                if (date == cursor.getString(2)){
                    val remind = Remind(cursor.getInt(0),cursor.getString(1),cursor.getString(2))
                    list.add(remind)
                }
            }
            return list
        }else{
            return getRemindDb()
        }
    }

}