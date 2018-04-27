package com.byren.kai.thinkdaily.utils

import android.annotation.SuppressLint
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.adapter.IncomeTypePopAdapter
import kotlinx.android.synthetic.main.income_pop.view.*

@SuppressLint("StaticFieldLeak")
object IncomePop {
    /*收入弹窗工具*/
    private val mContext = Common.mContext()
    private var incomeType = -1

}