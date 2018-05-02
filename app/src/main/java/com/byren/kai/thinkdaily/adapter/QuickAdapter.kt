package com.byren.kai.thinkdaily.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.beans.Quick
import com.byren.kai.thinkdaily.beans.Remind
import com.byren.kai.thinkdaily.utils.*
import kotlinx.android.synthetic.main.quick_item.view.*
import kotlinx.android.synthetic.main.remind_item.view.*
import java.util.*

class QuickAdapter(activity: Activity, list: ArrayList<Quick>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AdapterItemTouchHelper.ItemTouch {


//    提醒事件适配器

    private var context: Context? = null
    private var list: ArrayList<Quick> = ArrayList()

    init {
        this.context = activity
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuickHolder(LayoutInflater.from(context).inflate(R.layout.quick_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_quick_item.text = list[position].content
        setIcon(holder.itemView.iv_head_quick_item, position)
    }

    private fun setIcon(iv: ImageView, position: Int) {
        when (position % 5) {
            0 -> iv.setImageResource(R.drawable.fivehead)
            1 -> iv.setImageResource(R.drawable.fourhead)
            2 -> iv.setImageResource(R.drawable.threehead)
            3 -> iv.setImageResource(R.drawable.twohead)
            4 -> iv.setImageResource(R.drawable.onehead)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class QuickHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.findViewById(R.id.tv_quick_item)
        val headIv: ImageView = view.findViewById(R.id.iv_head_quick_item)
    }

    override fun itemDrag(stratPosition: Int, targetPosition: Int) {
    }

    override fun itemSwipe(position: Int) {
        if (quickDbManager.deleteQuickDb(list[position].id)) {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeInserted(position, list.size - position)
            notifyDataSetChanged()
            showToast.shortToast("删除成功")
        } else {
            showToast.shortToast("删除失败")
        }
    }

}