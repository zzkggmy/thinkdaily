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
import kotlinx.android.synthetic.main.incometype_pop_item.view.*

/**
 * Created by Lucas on 2018/3/28 .
 * 收入类型弹窗适配器
 */
class IncomeTypePopAdapter(activity: Activity, list: ArrayList<String>, val onClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var context: Context? = null
    private var list: ArrayList<String> = ArrayList()


    init {
        this.context = activity
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IncometypeHolder(LayoutInflater.from(context).inflate(R.layout.incometype_pop_item, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setIcon(holder!!.itemView.iv_pop_incometype_item, holder.itemView.tv_pop_incometype_item, position)

        holder.itemView.setOnClickListener {
            onClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setIcon(iv: ImageView, tv: TextView, position: Int) {
        when (position) {
            0 -> {
                iv.setImageResource(R.drawable.pigbank)
                tv.text = "一般"
            }
            1 -> {
                iv.setImageResource(R.drawable.wage)
                tv.text = "工资"
            }
            2 -> {
                iv.setImageResource(R.drawable.borrow)
                tv.text = "借入"
            }
            3 -> {
                iv.setImageResource(R.drawable.bonus)
                tv.text = "奖金"
            }
            4 -> {
                iv.setImageResource(R.drawable.redb)
                tv.text = "红包"
            }
            5 -> {
                iv.setImageResource(R.drawable.reim)
                tv.text = "报销"
            }
            6 -> {
                iv.setImageResource(R.drawable.invest)
                tv.text = "投资"
            }
            7 -> {
                iv.setImageResource(R.drawable.interest)
                tv.text = "利息"
            }
            8 -> {
                iv.setImageResource(R.drawable.lottery)
                tv.text = "彩票"
            }
            9 -> {
                iv.setImageResource(R.drawable.other)
                tv.text = "其他"
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position / (list.size - 1) == 1) 0 else 1
    }

    class IncometypeHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tv: TextView = view.findViewById(R.id.tv_pop_incometype_item)
        private val iv: ImageView = view.findViewById(R.id.iv_pop_incometype_item)

    }

}