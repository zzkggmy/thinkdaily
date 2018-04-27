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
import com.byren.kai.thinkdaily.beans.IncomeEntity
import com.byren.kai.thinkdaily.utils.AdapterItemTouchHelper
import com.byren.kai.thinkdaily.utils.incomeDbManager
import com.byren.kai.thinkdaily.utils.showToast
import kotlinx.android.synthetic.main.activity_addbill.*
import kotlinx.android.synthetic.main.income_item.view.*

/**
 * Created by Lucas on 2018/3/26 .
 * 收入适配器
 */
class IncomeAdapter(activitty: Activity, list: ArrayList<IncomeEntity>, val onClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AdapterItemTouchHelper.ItemTouch {



    private var context: Context? = null
    private var list: ArrayList<IncomeEntity> = ArrayList()

    init {
        this.context = activitty
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IncomeHolder(LayoutInflater.from(context).inflate(R.layout.income_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setTypeIcon(holder.itemView.iv_type_income_item, list[position].incomeType)
        holder.itemView.tv_amount_income_item.text = list.get(position).incomeAmount
        holder.itemView.tv_date_income_item.text = list.get(position).incomeDate
        holder.itemView.setOnClickListener {
            onClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setTypeIcon(iv_type_income_item: ImageView, incomeType: String) {
        when (incomeType) {
            "0" ->
                iv_type_income_item.setImageResource(R.drawable.pigbank)
            "1" ->
                iv_type_income_item.setImageResource(R.drawable.wage)
            "2" ->
                iv_type_income_item.setImageResource(R.drawable.borrow)
            "3" ->
                iv_type_income_item.setImageResource(R.drawable.bonus)
            "4" ->
                iv_type_income_item.setImageResource(R.drawable.redb)
            "5" ->
                iv_type_income_item.setImageResource(R.drawable.reim)
            "6" ->
                iv_type_income_item.setImageResource(R.drawable.invest)
            "7" ->
                iv_type_income_item.setImageResource(R.drawable.interest)
            "8" ->
                iv_type_income_item.setImageResource(R.drawable.lottery)
            "9" ->
                iv_type_income_item.setImageResource(R.drawable.other)
        }
    }

    class IncomeHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTv: TextView = view.findViewById(R.id.tv_date_income_item)
        val amountTv: TextView = view.findViewById(R.id.tv_amount_income_item)
        val typeIv: ImageView = view.findViewById(R.id.iv_type_income_item)
    }

    override fun itemDrag(stratPosition: Int, targetPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun itemSwipe(position: Int) {
        if (incomeDbManager.deleteIncome(list[position].incomeId)) {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size - position)
            notifyDataSetChanged()
            showToast.shortToast("删除成功")
        }else{
            showToast.shortToast("删除失败")
        }
    }
}