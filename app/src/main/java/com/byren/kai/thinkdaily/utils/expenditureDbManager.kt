package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.byren.kai.thinkdaily.beans.ExpenditureEntity
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
object expenditureDbManager {
    private val mContext = Common.mContext()
    private val expenditureDb: ExpenditureDb = ExpenditureDb(mContext, "expenditure.db", null, 1)
    private var expenditureSql: SQLiteDatabase? = null

    init {
        expenditureDb.writableDatabase
        expenditureSql = mContext.openOrCreateDatabase("expenditure.db", Context.MODE_PRIVATE, null)
    }

    @SuppressLint("Recycle")
    fun getExpenditure(): ArrayList<ExpenditureEntity> {
        val list: ArrayList<ExpenditureEntity> = ArrayList()
        val cursor: Cursor = expenditureSql!!.rawQuery("select * from expenditure", null, null)
        while (cursor.moveToNext()) {
            val expenditureEntity = ExpenditureEntity(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4))
            list.add(expenditureEntity)
        }
        cursor.close()
        return list
    }

    fun addExpenditure(expenditureType: String,
                       expenditureAmount: String,
                       expenditureDate: String,
                       expenditureNote: String): Boolean {
        expenditureDb.writableDatabase
        expenditureSql!!.beginTransaction()
        try {
            val values = ContentValues()
            values.put("expenditureType", expenditureType)
            values.put("expenditureAmount", expenditureAmount)
            values.put("expenditureDate", expenditureDate)
            values.put("expenditureNote", expenditureNote)
            expenditureSql!!.insert("expenditure", null, values)
            expenditureSql!!.setTransactionSuccessful()
            return true
        } catch (e: SQLiteConstraintException) {
            return false
        } finally {
            expenditureSql!!.endTransaction()
        }

    }

    fun deleteExpenditure(id: Int): Boolean {
        expenditureDb.writableDatabase
        try {
            expenditureSql!!.execSQL("delete from expenditure where _id =$id")
            return true
        }catch (e: SQLiteConstraintException){
            return false
        }
    }

    fun modifyBill(id: Int): ExpenditureEntity {
        var expenditureEntity: ExpenditureEntity? = null
        val cursor: Cursor = expenditureSql!!.rawQuery("select * from expenditure", null, null)
        while (cursor.moveToNext()) {

            if (cursor.getInt(0) == id) {
                expenditureEntity = ExpenditureEntity(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4))
            }
        }
        cursor.close()
        return expenditureEntity!!
    }

    fun modifyBillInfo(id: Int, expenditureAmount: String, expenditureType: String
                       , expenditureDate: String, expenditureNote: String): Boolean {
        expenditureDb.writableDatabase
        expenditureSql!!.beginTransaction()
        try {
            val values = ContentValues()
            values.put("expenditureType", expenditureType)
            values.put("expenditureAmount", expenditureAmount)
            values.put("expenditureDate", expenditureDate)
            values.put("expenditureNote", expenditureNote)

//            expenditureSql!!.execSQL("update expenditure set expenditureType =$expenditureType,expenditureAmount =$expenditureAmount,expenditureDate =$expenditureDate,expenditureNote =$expenditureNote where _id =$id")
            expenditureSql!!.update("expenditure", values, "_id=?", arrayOf(id.toString()))
            expenditureSql!!.setTransactionSuccessful()
            return true
        } catch (e: SQLiteConstraintException) {
            return false
        } finally {
            expenditureSql!!.endTransaction()
        }
    }

    fun getExpenditureNum(): Double {
        var num = 0.0
        val cursor: Cursor = expenditureSql!!.rawQuery("select * from expenditure", null, null)
        while (cursor.moveToNext()) {
            num += cursor.getString(2).toDouble()
        }
        cursor.close()
        return num
    }

    fun conditionQuery(type: String, date: String): ArrayList<ExpenditureEntity> {
        val list: ArrayList<ExpenditureEntity> = ArrayList()
        val cursor: Cursor = expenditureSql!!.rawQuery("select * from expenditure", null, null)
        if (type.toInt() >= 0 && date.isEmpty()) {
            while (cursor.moveToNext()) {
                if (type == cursor.getString(1)) {
                    val expenditureEntity: ExpenditureEntity = ExpenditureEntity(cursor.getInt(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4))
                    list.add(expenditureEntity)
                }
            }
            cursor.close()
            return list
        } else if (type.toInt() == -1 && date.isNotEmpty()) {
            while (cursor.moveToNext()) {
                if (date == cursor.getString(3)) {
                    val expenditureEntity: ExpenditureEntity = ExpenditureEntity(cursor.getInt(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4))
                    list.add(expenditureEntity)
                }
            }
            return list
        } else if (type.toInt() >= 0 && date.isNotEmpty()) {
            while (cursor.moveToNext()) {
                if (date == cursor.getString(3) && type == cursor.getString(1)) {
                    val expenditureEntity: ExpenditureEntity = ExpenditureEntity(cursor.getInt(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4))
                    list.add(expenditureEntity)
                }
            }
            return list
        } else {
            return getExpenditure()
        }
    }

}