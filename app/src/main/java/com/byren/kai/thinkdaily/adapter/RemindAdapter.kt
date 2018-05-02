package com.byren.kai.thinkdaily.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.beans.Remind
import com.byren.kai.thinkdaily.utils.AdapterItemTouchHelper
import com.byren.kai.thinkdaily.utils.remindDbManager
import com.byren.kai.thinkdaily.utils.showToast
import kotlinx.android.synthetic.main.remind_item.view.*
import java.util.*

class RemindAdapter(activity: Activity, list: ArrayList<Remind>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AdapterItemTouchHelper.ItemTouch {



//    提醒事件适配器

    private var context: Context? = null
    private var list: ArrayList<Remind> = ArrayList()

    init {
        this.context = activity
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RemindHolder(LayoutInflater.from(context).inflate(R.layout.remind_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_title_remind_item.text = list[position].title
        holder.itemView.tv_date_remind_item.text = list[position].date
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class RemindHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleTv: TextView = view.findViewById(R.id.tv_title_remind_item)
        val dateTv: TextView = view.findViewById(R.id.tv_date_remind_item)
    }

    override fun itemDrag(stratPosition: Int, targetPosition: Int) {
    }

    override fun itemSwipe(position: Int) {
        if (remindDbManager.deleteRemindDb(list[position].id)) {
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