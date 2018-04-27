package com.byren.kai.thinkdaily.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.beans.ExpenditureEntity
import com.byren.kai.thinkdaily.utils.AdapterItemTouchHelper
import com.byren.kai.thinkdaily.utils.expenditureDbManager
import com.byren.kai.thinkdaily.utils.showToast
import kotlinx.android.synthetic.main.activity_addbill.*
import kotlinx.android.synthetic.main.expenditure_item.view.*
import kotlinx.android.synthetic.main.income_item.view.*

class ExpenditureAdapter(activitty: Activity, list: ArrayList<ExpenditureEntity>, val onClick: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AdapterItemTouchHelper.ItemTouch {


//    支出适配器

    private var context: Context? = null
    private var list: ArrayList<ExpenditureEntity> = ArrayList()

    init {
        this.context = activitty
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExpenditureHolder(LayoutInflater.from(context).inflate(R.layout.expenditure_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setExpenditureIcon(holder!!.itemView.iv_type_expenditure_item, list[position].expenditureType)
        holder.itemView.tv_amount_expenditure_item.text = list.get(position).expenditureAmount
        holder.itemView.tv_date_expenditure_item.text = list.get(position).expenditureDate
        holder.itemView.setOnClickListener {
            onClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun setExpenditureIcon(iv: ImageView, type: String) {
        when (type) {
            "0" -> iv.setImageResource(R.drawable.normal)
            "1" -> iv.setImageResource(R.drawable.dining)
            "2" -> iv.setImageResource(R.drawable.traffic)
            "3" -> iv.setImageResource(R.drawable.shop)
            "4" -> iv.setImageResource(R.drawable.apparel)
            "5" -> iv.setImageResource(R.drawable.house)
            "6" -> iv.setImageResource(R.drawable.dailyuse)
            "7" -> iv.setImageResource(R.drawable.cosmetic)
            "8" -> iv.setImageResource(R.drawable.elec)
            "9" -> iv.setImageResource(R.drawable.snacks)
            "10" -> iv.setImageResource(R.drawable.edu)
            "11" -> iv.setImageResource(R.drawable.commun)
            "12" -> iv.setImageResource(R.drawable.medical)
            "13" -> iv.setImageResource(R.drawable.travel)
            "14" -> iv.setImageResource(R.drawable.humanity)
            "15" -> iv.setImageResource(R.drawable.other)
        }

    }

    class ExpenditureHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeIv: ImageView = view.findViewById(R.id.iv_type_expenditure_item)
        val dateTv: TextView = view.findViewById(R.id.tv_date_expenditure_item)
        val amountTv: TextView = view.findViewById(R.id.tv_amount_expenditure_item)

    }

    override fun itemDrag(stratPosition: Int, targetPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun itemSwipe(position: Int) {
        if (expenditureDbManager.deleteExpenditure(list[position].expenditureId)){
            showToast.shortToast("删除成功")
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,list.size - position)
            notifyDataSetChanged()
        }else{
            showToast.shortToast("删除失败")
        }
    }
}