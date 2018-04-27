package com.byren.kai.thinkdaily.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import kotlinx.android.synthetic.main.expendituretype_pop_item.view.*

/**
 * Created by Lucas on 2018/3/28 .
 * 支出类型弹窗适配器
 */
class ExpenditureTypePopAdapter(activity: Activity, list: ArrayList<String>, val onClick: (View, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var context: Context? = null
    private var list: ArrayList<String> = ArrayList()

    init {
        this.context = activity
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IncometypeHolder(LayoutInflater.from(context).inflate(R.layout.expendituretype_pop_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setIcon(holder.itemView.tv_addbill_pop_expendituretype_item, holder.itemView.iv_addbill_pop_expendituretype_item, position)
        holder.itemView.setOnClickListener {
            onClick(holder.itemView, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setIcon(tv: TextView, iv: ImageView, position: Int) {
        when (position) {
            0 -> {
                iv.setImageResource(R.drawable.normal)
                tv.text = "一般"
            }
            1 -> {
                iv.setImageResource(R.drawable.dining)
                tv.text = "用餐"
            }
            2 -> {
                iv.setImageResource(R.drawable.traffic)
                tv.text = "交通"
            }
            3 -> {
                iv.setImageResource(R.drawable.shop)
                tv.text = "购物"
            }
            4 -> {
                iv.setImageResource(R.drawable.apparel)
                tv.text = "服饰"
            }
            5 -> {
                iv.setImageResource(R.drawable.house)
                tv.text = "住房"
            }
            6 -> {
                iv.setImageResource(R.drawable.dailyuse)
                tv.text = "日用品"
            }
            7 -> {
                iv.setImageResource(R.drawable.cosmetic)
                tv.text = "化妆品"
            }
            8 -> {
                iv.setImageResource(R.drawable.elec)
                tv.text = "电子产品"
            }
            9 -> {
                iv.setImageResource(R.drawable.snacks)
                tv.text = "零食"
            }
            10 -> {
                iv.setImageResource(R.drawable.edu)
                tv.text = "教育"
            }
            11 -> {
                iv.setImageResource(R.drawable.commun)
                tv.text = "通讯"
            }
            12 -> {
                iv.setImageResource(R.drawable.medical)
                tv.text = "医疗"
            }
            13 -> {
                iv.setImageResource(R.drawable.travel)
                tv.text = "旅游"
            }
            14 -> {
                iv.setImageResource(R.drawable.humanity)
                tv.text = "人情"
            }

            15 -> {
                iv.setImageResource(R.drawable.other)
                tv.text = "其他"
            }
        }
    }

    class IncometypeHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tv: TextView = view.findViewById(R.id.tv_addbill_pop_expendituretype_item)
        private val iv: ImageView = view.findViewById(R.id.iv_addbill_pop_expendituretype_item)

    }

}