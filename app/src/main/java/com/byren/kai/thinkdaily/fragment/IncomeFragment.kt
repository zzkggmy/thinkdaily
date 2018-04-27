package com.byren.kai.thinkdaily.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.byren.kai.thinkdaily.R
import com.byren.kai.thinkdaily.activity.AddBillActivity
import com.byren.kai.thinkdaily.activity.StatisticsActivity
import com.byren.kai.thinkdaily.adapter.IncomeAdapter
import com.byren.kai.thinkdaily.adapter.IncomeTypePopAdapter
import com.byren.kai.thinkdaily.utils.*
import kotlinx.android.synthetic.main.datepopup.view.*
import kotlinx.android.synthetic.main.fragment_income.view.*
import kotlinx.android.synthetic.main.income_pop.view.*
import java.util.*

class IncomeFragment : Fragment(), View.OnClickListener {
    private var incomeDate = ""
    private var incomeType = -1
    private var handler: Handler? = null
    private var adapter: IncomeAdapter? = null
    private val mContext = Common.mContext()
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(mContext)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(mContext, R.layout.fragment_income, null)
        view.ll_classification_income_frag.setOnClickListener(this)
        view.ll_date_income_frag.setOnClickListener(this)
        view.iv_complete_income_frag.setOnClickListener(this)
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    1 -> {
                        if (incomeType >= 0) {
                            view.iv_complete_income_frag.visibility = View.VISIBLE
                        } else if (incomeDate != "日期") {
                            view.iv_complete_income_frag.visibility = View.VISIBLE
                        } else {
                            view.iv_complete_income_frag.visibility = View.GONE
                        }
                    }
                }
            }
        }
        return view
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_classification_income_frag -> {
                setBackGroundAlpha.set(mContext as FragmentActivity, 0.3f)
                showType(mContext, v.iv_classfication_income_frag, v.tv_classification_income_frag)

            }
            R.id.ll_date_income_frag -> {
                v.tv_date_income_frag.showDate(mContext as FragmentActivity,handler!!)
            }
            R.id.iv_complete_income_frag ->{
                Log.d("income",incomeDate + incomeType)
                adapter = IncomeAdapter(mContext as FragmentActivity, incomeDbManager.conditionalQuery(incomeType.toString(),incomeDate), { position ->
                    val intent = Intent(activity, AddBillActivity::class.java)
                    intent.putExtra("id", incomeDbManager.getIncome()[position].incomeId)
                    startActivity(intent)
                    SpUtils.spSetType(1)
                })
                val helper = AdapterItemTouchHelper(adapter)
                val itemTouchHelper = ItemTouchHelper(helper)
                itemTouchHelper.attachToRecyclerView(view!!.rv_income_frag)
                view!!.rv_income_frag.adapter = adapter
                view!!.rv_income_frag.layoutManager = linearLayoutManager
                view!!.rv_income_frag.adapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter = IncomeAdapter(activity!!, incomeDbManager.getIncome(), { position ->
            val intent = Intent(activity, AddBillActivity::class.java)
            intent.putExtra("id", incomeDbManager.getIncome()[position].incomeId)
            startActivity(intent)
            SpUtils.spSetType(1)
        })
        val helper = AdapterItemTouchHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(helper)
        itemTouchHelper.attachToRecyclerView(view!!.rv_income_frag)
        view!!.rv_income_frag.adapter = adapter
        view!!.rv_income_frag.layoutManager = linearLayoutManager
        view!!.rv_income_frag.adapter!!.notifyDataSetChanged()
    }

    fun showType(activity: FragmentActivity, iv: ImageView, tv: TextView) {
        val incomeTypeList: ArrayList<String> = ArrayList()
        val gridLayoutManager = GridLayoutManager(getActivity(), 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        for (i in 0..10) {
            incomeTypeList.add("" + i)
        }
        val msg = Message()
        msg.what = 1
        val view: View = LayoutInflater.from(getActivity()).inflate(R.layout.income_pop, null, false)
        val window = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        view.rv_income_pop.adapter = IncomeTypePopAdapter(activity, incomeTypeList, { position ->
            SpUtils.spSetIncomeClassification(position)
            setClassification(iv, tv, position)
            incomeType = position
            handler!!.sendMessage(msg)
            window.dismiss()
        })
        window.setOnDismissListener {
            setBackGroundAlpha.set(activity, 1.0f)
        }
        view.rv_income_pop.layoutManager = gridLayoutManager
        view.rv_income_pop.adapter.notifyDataSetChanged()
        window.isOutsideTouchable = true
        window.isTouchable = true
        window.update()
        window.showAtLocation(activity.window.decorView, Gravity.BOTTOM, 0, 0)
    }

    private fun setClassification(iv: ImageView, tv: TextView, position: Int) {
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

    fun TextView.showDate(activity: Activity,handler: Handler) {
        val time = StringBuffer()
        setBackGroundAlpha.set(activity, 0.3f)
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        var year1 = calendar.get(Calendar.YEAR)
        var month1 = calendar.get(Calendar.MONTH)
        var day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val contentView: View = LayoutInflater.from(activity).inflate(R.layout.datepopup, null, false)
        val dateWindow = PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        contentView.dp_date_popup.init(contentView.dp_date_popup.year, contentView.dp_date_popup.month, contentView.dp_date_popup.dayOfMonth
        ) { view, year, monthOfYear, dayOfMonth ->
            year1 = year
            month1 = monthOfYear + 1
            day1 = dayOfMonth
        }
        contentView.btn_confirm_date_popup.setOnClickListener {
            val msg = Message()
            msg.what = 1
            time.append(year1)
            time.append("-")
            time.append(month1)
            time.append("-")
            time.append(day1)
            text = time.toString()
            SpUtils.spSetDate(time.toString())
            incomeDate = time.toString()
            Log.d("bir", time.toString())
            dateWindow.dismiss()
            setBackGroundAlpha.set(activity, 1.0f)
            handler.sendMessage(msg)
        }
        contentView.btn_cancel_date_popup.setOnClickListener {
            dateWindow.dismiss()
            setBackGroundAlpha.set(activity, 1.0f)
        }

        dateWindow.setOnDismissListener {
            setBackGroundAlpha.set(activity, 1.0f)
            time.delete(0, time.length)
            SpUtils.spSetDate("")
        }
        dateWindow.isOutsideTouchable = false
        dateWindow.isTouchable = true
        dateWindow.update()
        dateWindow.showAtLocation(activity.window.decorView, Gravity.BOTTOM, 0, 0)
    }
}