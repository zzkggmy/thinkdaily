package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import com.byren.kai.thinkdaily.beans.ExpenditureEntity
import com.byren.kai.thinkdaily.beans.IncomeEntity
import android.provider.SyncStateContract.Helpers.update


@SuppressLint("StaticFieldLeak")
object incomeDbManager {
    private val mContext = Common.mContext()
    private val incomeDb: IncomeDb = IncomeDb(mContext, "income.db", null, 1)
    private var incomeSql: SQLiteDatabase? = null

    init {
        incomeDb.writableDatabase
        incomeSql = mContext.openOrCreateDatabase("income.db", Context.MODE_PRIVATE, null)
    }

    fun getIncome(): ArrayList<IncomeEntity> {
        val list: ArrayList<IncomeEntity> = ArrayList()
        val cursor: Cursor = incomeSql!!.rawQuery("select * from income", null, null)
        while (cursor.moveToNext()) {
            val incomeEntity: IncomeEntity = IncomeEntity(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4))
            if (cursor.getInt(0) >= 1) {
                list.add(incomeEntity)
            }
        }
        cursor.close()
        return list
    }

    fun addIncome(incomeType: String, incomeAmount: String,
                  incomeDate: String, incomeNote: String): Boolean {
        incomeSql!!.beginTransaction()
        try {
            val values = ContentValues()
            values.put("incomeType", incomeType)
            values.put("incomeAmount", incomeAmount)
            values.put("incomeDate", incomeDate)
            values.put("incomeNote", incomeNote)
            incomeSql!!.insert("income", null, values)
            incomeSql!!.setTransactionSuccessful()
            return true
        } catch (e: SQLiteConstraintException) {
            return false
        } finally {
            incomeSql!!.endTransaction()
        }

    }


    fun deleteIncome(id: Int) : Boolean {
        incomeDb.writableDatabase
        try {
            incomeSql!!.execSQL("delete from income where _id =$id")
            return true
        }catch (e: SQLiteConstraintException){
            return false
        }
    }

    fun modifyBill(id: Int): IncomeEntity {
        var incomeEntity: IncomeEntity? = null
        val cursor: Cursor = incomeSql!!.rawQuery("select * from income", null, null)
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == id) {
                incomeEntity = IncomeEntity(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4))
            }
        }
        cursor.close()
        return incomeEntity!!
    }

    fun modifyBillInfo(id: Int, incomeAmount: String, incomeType: String
                       , incomeDate: String, incomeNote: String): Boolean {
        incomeDb.writableDatabase
        incomeSql!!.beginTransaction()
        try {
            val values = ContentValues()
            values.put("incomeType", incomeType)
            values.put("incomeAmount", incomeAmount)
            values.put("incomeDate", incomeDate)
            values.put("incomeNote", incomeNote)

//                    incomeSql!!.execSQL("update income set incomeType =$incomeType,incomeAmount =$incomeAmount,incomeDate =$incomeDate,incomeNote =$incomeNote where _id =$id")
            incomeSql!!.update("income", values, "_id=?", arrayOf(id.toString()))
            incomeSql!!.setTransactionSuccessful()
            return true
        } catch (e: SQLiteConstraintException) {
            return false
        } finally {
            incomeSql!!.endTransaction()
        }
    }

    fun conditionalQuery(incomeType: String, date: String): ArrayList<IncomeEntity> {
        val cursor: Cursor = incomeSql!!.rawQuery("select * from income", null, null)
        val list: ArrayList<IncomeEntity> = ArrayList()
        if (incomeType.toInt() == -1 && date.isNotEmpty()) {
            while (cursor.moveToNext()) {
                if (cursor.getString(3) == date) {
                    val incomeEntity: IncomeEntity = IncomeEntity(cursor.getInt(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4))
                    list.add(incomeEntity)
                }
            }
            return list
        } else if (incomeType.toInt() >= 0 && date.isEmpty()) {
            while (cursor.moveToNext()) {
                if (cursor.getString(1) == incomeType) {
                    val incomeEntity: IncomeEntity = IncomeEntity(cursor.getInt(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4))
                    list.add(incomeEntity)
                }
            }
            return list
        } else if (incomeType.toInt() >= 0 && date.isNotEmpty()) {
            while (cursor.moveToNext()) {
                if (cursor.getString(1) == incomeType && cursor.getString(3) == date) {
                    val incomeEntity: IncomeEntity = IncomeEntity(cursor.getInt(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4))
                    list.add(incomeEntity)
                }
            }
            return list
        } else {
            return getIncome()
        }
    }
}