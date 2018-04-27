package com.byren.kai.thinkdaily.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by kai on 18-4-14.
 */
object SpUtils {
    val sp: SharedPreferences = Common.mContext().getSharedPreferences("thinkDaily", Context.MODE_PRIVATE)

    fun spSetDate(date: String) {
        sp.edit().putString("date", date).apply()
    }
    val date = fun(): String = sp.getString("date", "2018")

    fun spSetType(type: Int) {
        sp.edit().putInt("type", type).apply()
    }
    val type = fun(): Int = sp.getInt("type", 1)

    fun spSetRvMode(mode: Int) {
        sp.edit().putInt("mode", mode).apply()
    }
    val mode = fun(): Int = sp.getInt("mode", 1)

    fun spSetBudge(budge: String) {
        sp.edit().putString("budge", budge).apply()
    }
    val budge = fun(): String = sp.getString("budge", "暂无")

    fun spSetIncome(income: String) {
        sp.edit().putString("income", income).apply()
    }
    val income = fun(): String = sp.getString("income", "暂无")

    fun spSetIncomeClassification(incomeClassification: Int) {
        sp.edit().putInt("incomeClassification", incomeClassification).apply()
    }
    val incomeClassification = fun(): Int = sp.getInt("incomeClassification", 0)
}